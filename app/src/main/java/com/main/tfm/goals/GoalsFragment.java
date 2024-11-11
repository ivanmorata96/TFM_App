package com.main.tfm.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.main.tfm.support.UserContent;
import com.main.tfm.R;
import com.main.tfm.support.database.UserDB;

import java.util.ArrayList;
import java.util.HashMap;

public class GoalsFragment extends Fragment {

    private int moviesTarget, moviesCompleted, showsTarget, showsCompleted, videogamesTarget, videogamesCompleted, booksTarget, booksCompleted;
    private HashMap<String, Integer> goals = new HashMap<>();
    ProgressBar moviesPB, tvPB, videogamesPB, booksPB;
    TextView titleView, moviesProgress, showsProgress, videogamesProgress, booksProgress;
    AppCompatButton setGoalsButton, editGoalsButton;
    private UserDB db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        titleView = view.findViewById(R.id.goalsMainTitleView);
        setGoalsButton = view.findViewById(R.id.setGoalsButton);
        db = new UserDB(getContext());
        if(!db.checkIfGoalsAreSet()){
            titleView.setText("You currently don't have any goals set for this year! Would you like to?");
            setGoalsButton.setVisibility(View.VISIBLE);
            editGoalsButton.setVisibility(View.GONE);
            setGoalsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSetGoalsDialog();
                    reloadFragment();
                }
            });
        }
        checkUpdatesOnGoals(db);
        goals = db.retrieveAllGoals();
        moviesPB = view.findViewById(R.id.moviesPB);
        tvPB = view.findViewById(R.id.TVPB);
        videogamesPB = view.findViewById(R.id.videogamesPB);
        booksPB = view.findViewById(R.id.booksPB);
        moviesProgress = view.findViewById(R.id.moviesPBProgressView);
        showsProgress = view.findViewById(R.id.TVPBProgressView);
        videogamesProgress = view.findViewById(R.id.videogamesPBProgressView);
        booksProgress = view.findViewById(R.id.booksPBProgressView);
        editGoalsButton = view.findViewById(R.id.editGoalsButton);
        if(!goals.isEmpty()){
            moviesTarget = goals.get("moviesTarget");
            moviesCompleted = goals.get("moviesCompleted");
            showsTarget = goals.get("showsTarget");
            showsCompleted = goals.get("showsCompleted");
            videogamesTarget = goals.get("videogamesTarget");
            videogamesCompleted = goals.get("videogamesCompleted");
            booksTarget = goals.get("booksTarget");
            booksCompleted = goals.get("booksCompleted");
        }else {
            moviesTarget = moviesCompleted = showsTarget = showsCompleted = videogamesTarget = videogamesCompleted = booksTarget = booksCompleted = 0;
        }
        moviesPB.setMax(moviesTarget);
        moviesPB.setProgress(moviesCompleted);
        moviesProgress.setText(moviesCompleted + "/" + moviesTarget);
        tvPB.setMax(showsTarget);
        tvPB.setProgress(showsCompleted);
        showsProgress.setText(showsCompleted + "/" + showsTarget);
        videogamesPB.setMax(videogamesTarget);
        videogamesPB.setProgress(videogamesCompleted);
        videogamesProgress.setText(videogamesCompleted + "/" + videogamesTarget);
        booksPB.setMax(booksTarget);
        booksPB.setProgress(booksCompleted);
        booksProgress.setText(booksCompleted + "/" + booksTarget);
        editGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditGoalsDialog(moviesTarget, showsTarget, videogamesTarget, booksTarget);
                reloadFragment();
            }
        });
        return view;
    }

    private void showSetGoalsDialog(){
        EditText moviesGoal, tvShowsGoal, videogamesGoal, booksGoal;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.set_goals_dialog_layout, null);
        moviesGoal = dialogView.findViewById(R.id.movieGoalET);
        tvShowsGoal = dialogView.findViewById(R.id.tvShowGoalET);
        videogamesGoal = dialogView.findViewById(R.id.videogameGoalET);
        booksGoal = dialogView.findViewById(R.id.booksGoalET);
        new AlertDialog.Builder(getContext())
                .setTitle("Set your media goals.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    int movies = Integer.parseInt(moviesGoal.getText().toString());
                    int tvShows = Integer.parseInt(tvShowsGoal.getText().toString());
                    int videogames = Integer.parseInt(videogamesGoal.getText().toString());
                    int books = Integer.parseInt(booksGoal.getText().toString());
                    db.addUserGoals(movies, tvShows, videogames, books);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditGoalsDialog(int moviesTarget, int showsTarget, int videogamesTarget, int booksTarget){
        EditText moviesGoal, tvShowsGoal, videogamesGoal, booksGoal;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.set_goals_dialog_layout, null);
        moviesGoal = dialogView.findViewById(R.id.movieGoalET);
        tvShowsGoal = dialogView.findViewById(R.id.tvShowGoalET);
        videogamesGoal = dialogView.findViewById(R.id.videogameGoalET);
        booksGoal = dialogView.findViewById(R.id.booksGoalET);
        moviesGoal.setText(Integer.toString(moviesTarget));
        tvShowsGoal.setText(Integer.toString(showsTarget));
        videogamesGoal.setText(Integer.toString(videogamesTarget));
        booksGoal.setText(Integer.toString(booksTarget));
        new AlertDialog.Builder(getContext())
                .setTitle("Set your media goals.")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    int movies = Integer.parseInt(moviesGoal.getText().toString());
                    int tvShows = Integer.parseInt(tvShowsGoal.getText().toString());
                    int videogames = Integer.parseInt(videogamesGoal.getText().toString());
                    int books = Integer.parseInt(booksGoal.getText().toString());
                    db.editGoalTable(movies, tvShows, videogames, books);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void reloadFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }
    }

    private void checkUpdatesOnGoals(UserDB db) {
        int movies = 0, shows = 0, videogames = 0, books = 0;
        ArrayList<UserContent> content = db.retrieveContentByStatus("Completed");
        for (UserContent u : content) {
            switch (u.getType()) {
                case "movie":
                    movies++;
                    break;
                case "tvshow":
                    shows++;
                    break;
                case "videogame":
                    videogames++;
                    break;
                case "book":
                    books++;
                    break;
            }
        }
        db.updateGoalTable("movie", movies);
        db.updateGoalTable("tvshow", shows);
        db.updateGoalTable("videogame", videogames);
        db.updateGoalTable("book", books);
    }
}
