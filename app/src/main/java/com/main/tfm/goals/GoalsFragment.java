package com.main.tfm.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.main.tfm.APIAccess.UserContent;
import com.main.tfm.R;
import com.main.tfm.database.UserDB;

public class GoalsFragment extends Fragment {
    TextView titleView;
    AppCompatButton setGoalsButton;
    private UserDB db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        titleView = view.findViewById(R.id.goalsMainTitleView);
        setGoalsButton = view.findViewById(R.id.setGoalsButton);
        db = new UserDB(getContext());
        if(!db.checkIfGoalsAreSet()){
            titleView.setText("You currently don't have any goals set for this year! Would you like to?");
            setGoalsButton.setVisibility(View.VISIBLE);
            setGoalsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSetGoalsDialog();

                }
            });
        }
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
                    reloadFragment();
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
}
