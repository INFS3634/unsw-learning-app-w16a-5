package au.edu.unsw.infs3634.unswlearning.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Model.QuestionsModel;
import au.edu.unsw.infs3634.unswlearning.R;

public class QuizFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "QUIZ_FRAGMENT_LOG";
    private NavController navController;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currUserId;
    private String quizTitle;
    private String quizId;
    private TextView quiz_title, quiz_questions_number, quiz_question_time, quiz_questions, quiz_question_feedback;
    private Button quiz_option_one, quiz_option_two, quiz_option_three, quiz_next_btn;
    private ProgressBar quiz_question_progress;
    private ImageButton quiz_close_btn;
    private CountDownTimer countDownTimer;

    private boolean canAnswer = false;
    private int currQuestion = 0;

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int missedAnswers = 0;



    private List<QuestionsModel> questionsToAnswer = new ArrayList<>();
    //parsed from the previous fragment DetailActivity
    private long totalQuestionsToAnswer;
    //pulling the list of questions from QuestionsModel class and are picked from allQuestionsList
    private List<QuestionsModel> allQuestionsList = new ArrayList<>();


    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        firebaseAuth = FirebaseAuth.getInstance();

        //get the user ID if the user is not null
        if(firebaseAuth.getCurrentUser() != null) {
            currUserId = firebaseAuth.getCurrentUser().getUid();
        }
        else {
            //back to home page

        }
        //initialise firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        //UI initialise
        quiz_title = view.findViewById(R.id.quiz_title);
        quiz_questions_number = view.findViewById(R.id.quiz_question_number);
        quiz_questions = view.findViewById(R.id.quiz_question);
        quiz_question_time = view.findViewById(R.id.quiz_question_time);
        quiz_question_feedback = view.findViewById(R.id.quiz_question_feedback);
        quiz_option_one = view.findViewById(R.id.quiz_option_one);
        quiz_option_two = view.findViewById(R.id.quiz_option_two);
        quiz_option_three = view.findViewById(R.id.quiz_option_three);
        quiz_next_btn = view.findViewById(R.id.quiz_next_btn);
        quiz_question_progress = view.findViewById(R.id.quiz_question_progress);
        quiz_close_btn = view.findViewById(R.id.quiz_close_btn);

        //close button to navigate to fragment_list
        quiz_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_quizFragment_to_listFragment);
            }
        });

        //similar to get Intent for Activites, this is the equivalent for Fragments
        //arguments are store in nav_graph
        //this argument is parsed quiz Id, quiz title and total questions from detailFragment
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        quizTitle =  QuizFragmentArgs.fromBundle(getArguments()).getQuizTitle();
        totalQuestionsToAnswer = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();

        //Querying Firestore data
        firebaseFirestore.collection("QuizList")
                .document(quizId).collection("Questions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            //all questions added to list
                            allQuestionsList = task.getResult().toObjects(QuestionsModel.class);
                            //pickQuestions
                            pickQuestions();
                            //calls method that sets UI elements based on XML
                            loadUi();


                        }
                        else {
                            //error getting the questions
                            quiz_title.setText("Error loading data");
                        }
                    }
                });
        //set answer button onClickListener
        quiz_option_one.setOnClickListener(this);
        quiz_option_two.setOnClickListener(this);
        quiz_option_three.setOnClickListener(this);
        quiz_next_btn.setOnClickListener(this);
    }

    //method to apply the positions of the random questions to our allQuestionsList
    private void pickQuestions() {
        for (int i = 0; i < totalQuestionsToAnswer; i++) {
            int randomNumber = getRandomInteger(allQuestionsList.size(), 0);
            //adds the question of that position to allQuestionList
            questionsToAnswer.add(allQuestionsList.get(randomNumber));
            //removing the question we just added to questionsToAnswer from the allQuestionsList ensures no duplicate questions are picked
            allQuestionsList.remove(randomNumber);
        }
    }

    //algorithm to pick X number of random questions from our Questions database
    public static int getRandomInteger(int maximum, int minimum) {
        //Math.random is a native library built into Java
        return ((int) (Math.random()*(maximum-minimum))) + minimum;
    }

    private void loadUi() {
        //load UI once the quiz data has been loaded;
        quiz_title.setText(quizTitle);
        quiz_questions.setText("Load First Question");

        //calls method that allows users to press the answer buttons and
        enableOptions();

        //load the first question;
        loadQuestion(1);
    }

    private void loadQuestion(int position) {

        //converts the question postion to string as setText does not take property type integer
        String questionNum = String.valueOf(position);
        quiz_questions_number.setText(questionNum);
        currQuestion = position;

        //load question text
        //a position - 1 is applied to ensure the first question doesnt get skipped from the Firebase database due to zero-based indexing
        quiz_questions.setText(questionsToAnswer.get(position-1).getQuestion());

        //load options
        quiz_option_one.setText(questionsToAnswer.get(position-1).getOption_a());
        quiz_option_two.setText(questionsToAnswer.get(position-1).getOption_b());
        quiz_option_three.setText(questionsToAnswer.get(position-1).getOption_c());

        //questions are now loaded, hence user can answer
        canAnswer = true;

        //start the countdown timer
        startTimer(position);

    }

    private void startTimer(int position) {
        //set timer text
        Long timeToAnswer = questionsToAnswer.get(position-1).getTimer();
        quiz_question_time.setText(timeToAnswer.toString());

        //show timer progressbar
        quiz_question_progress.setVisibility(View.VISIBLE);

        //start countdown in milliseconds
        countDownTimer = new CountDownTimer(timeToAnswer*1000, 10) {

            @Override
            public void onTick(long tillFinished) {
                //update the time
                quiz_question_time.setText(tillFinished/1000 + "");

                //Progress converted to percentage
                Long percent = tillFinished / (timeToAnswer*10);
                quiz_question_progress.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                //Time is up, cannot answer the questions anymore
                canAnswer = false;
                quiz_question_feedback.setText("Time's up! No answer submitted.");
                missedAnswers++;
                showNextBtn();
            }
        };

        //start the countdown
        countDownTimer.start();
    }

    private void enableOptions() {
        //show all the choice buttons
        quiz_option_one.setVisibility(View.VISIBLE);
        quiz_option_two.setVisibility(View.VISIBLE);
        quiz_option_three.setVisibility(View.VISIBLE);

        //enable option buttons
        quiz_option_one.setEnabled(true);
        quiz_option_two.setEnabled(true);
        quiz_option_three.setEnabled(true);

        //hide feedback and next button
        quiz_question_feedback.setVisibility(View.INVISIBLE);
        quiz_next_btn.setVisibility(View.INVISIBLE);
        quiz_next_btn.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        //check the button that has been pressed by comparing the view IDs
        //switch statement for the different result options
        switch (view.getId()) {
            case R.id.quiz_option_one:
                answerSelected(quiz_option_one);
                break;
            case R.id.quiz_option_two:
                answerSelected(quiz_option_two);
                break;
            case R.id.quiz_option_three:
                answerSelected(quiz_option_three);
                break;
            case R.id.quiz_next_btn:
                //checks to ensure this is the last question
                if(currQuestion == totalQuestionsToAnswer) {
                    submitQuiz();

                }
                //increments the currQuestions
                else {
                    currQuestion++;
                    loadQuestion(currQuestion);
                    resetQuestions();
                }

                break;
        }
    }

    private void submitQuiz() {
        //hashmap takes key-value pairs which are then fed back to Firebase
        //this ensures an acceptable and digestable format for Firebase
        HashMap<String, Object> resultMap = new HashMap<>();
        //hashmap e.g. Key: "correct", value: correctAnswers
        resultMap.put("correct", correctAnswers);
        resultMap.put("incorrect", incorrectAnswers);
        resultMap.put("missed", missedAnswers);
        //accessing Firebase
        firebaseFirestore.collection("QuizList")
                //create a new collection within QuizList to house the key-value pairs from the hashmap
                .document(quizId).collection("Results")
                .document(currUserId).set(resultMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //go to results page
                            QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment();
                            action.setQuizId(quizId);
                            navController.navigate(action);
                        }
                        else {
                            //show the error
                            quiz_title.setText(task.getException().getMessage());
                        }
                    }
                });
    }

    //reset the format for the UI back to normal for the next question
    private void resetQuestions() {
        quiz_option_one.setBackgroundColor(getResources().getColor(R.color.purple_500));
        quiz_option_two.setBackgroundColor(getResources().getColor(R.color.purple_500));
        quiz_option_three.setBackgroundColor(getResources().getColor(R.color.purple_500));

        quiz_question_feedback.setVisibility(View.INVISIBLE);
        quiz_next_btn.setVisibility(View.INVISIBLE);
        quiz_next_btn.setEnabled(false);
    }

    //compares the user selected answer to the answer specified in Firebase
    private void answerSelected(Button selectedButton) {
        //Check answer
        if (canAnswer) {
            if(questionsToAnswer.get(currQuestion-1).getAnswer().equals(selectedButton.getText())) {
                //Correct answer
                correctAnswers++;
                selectedButton.setBackgroundColor(getResources().getColor(R.color.correct));

                //set feedback text
                quiz_question_feedback.setText("Correct Answer");

            }
            else {
                //incorrect answer
                incorrectAnswers++;
                selectedButton.setBackgroundColor(getResources().getColor(R.color.incorrect));
                quiz_question_feedback.setText("Incorrect Answer \n Correct Answer: " + questionsToAnswer.get(currQuestion-1).getAnswer());
            }
            canAnswer = false;

            //stop the timer
            countDownTimer.cancel();

            //show next button
            showNextBtn();
        }
    }

    //method to enable the next question button after user has submitted his answer
    private void showNextBtn() {
        //check for last question
        if(currQuestion == totalQuestionsToAnswer) {
            quiz_next_btn.setText("Submit Quiz");

        }
        quiz_question_feedback.setVisibility(View.VISIBLE);
        quiz_next_btn.setVisibility(View.VISIBLE);
        quiz_next_btn.setEnabled(true);
    }
}


