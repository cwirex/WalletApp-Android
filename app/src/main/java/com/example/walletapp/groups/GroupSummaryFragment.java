package com.example.walletapp.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.walletapp.DAO;
import com.example.walletapp.Group;
import com.example.walletapp.R;

import java.util.Locale;
import java.util.Objects;

public class GroupSummaryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    ImageView btn_back;
    FrameLayout splitFrame;
    private String gid;
    private Group currentGroup;
    private Button btn_calculate;

    public GroupSummaryFragment() {
        // Required empty public constructor
    }

    public static GroupSummaryFragment newInstance(String gid) {
        GroupSummaryFragment fragment = new GroupSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, gid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gid = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_summary, container, false);

        TextView tv_usersCount = view.findViewById(R.id.grSumm_tv_usersInGroup);
        TextView tv_expensesCount = view.findViewById(R.id.grSumm_tv_numberOfExpenses);
        TextView tv_totalValue = view.findViewById(R.id.grSumm_tv_totalValue);
        btn_calculate = view.findViewById(R.id.grSumm_btn_calculate);
        btn_back = view.findViewById(R.id.grSummary_btn_back);
        splitFrame = view.findViewById(R.id.grSumm_frame);

        for (Group g : Objects.requireNonNull(DAO.getInstance().groups.getValue())) {
            if (gid.equals(g.getId())) {
                currentGroup = g;
            }
        }
        assert currentGroup != null;
        double tot = 0;
        for (GroupExpenseDTO g : currentGroup.expenses.getValue()) {
            tot += Double.parseDouble(g.cost);
        }

        String usersCount = String.valueOf(currentGroup.users.getValue().size());
        String expensesCount = String.valueOf(currentGroup.expenses.getValue().size());
        String totalValue = String.format(Locale.US, "%.2f", tot);

        tv_usersCount.setText(usersCount);
        tv_expensesCount.setText(expensesCount);
        tv_totalValue.setText(totalValue);

        btn_calculate.setOnClickListener(l -> {
            splitFrame.setVisibility(View.VISIBLE);
            SplitFragment fragment = SplitFragment.newInstance(gid);
            FragmentTransaction FT = getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.grSumm_frame, fragment);
            FT.commit();
            btn_calculate.setVisibility(View.GONE);
        });

        btn_back.setOnClickListener(back -> finish());
        return view;
    }

    private void finish() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }

}