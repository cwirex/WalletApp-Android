package com.example.walletapp.groups;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletapp.DAO;
import com.example.walletapp.Group;
import com.example.walletapp.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SplitFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    protected ArrayList<SplitObject> result = new ArrayList<>();
    protected SplitAdapter adapter;
    ArrayList<String> users;
    ArrayList<GroupExpenseDTO> expenses;
    private String gid;
    private Group currentGroup;
    private ListView listView;

    public SplitFragment() {
        // Required empty public constructor
    }

    public static SplitFragment newInstance(String gid) {
        SplitFragment fragment = new SplitFragment();
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
        View view = inflater.inflate(R.layout.fragment_split, container, false);
        listView = view.findViewById(R.id.split_listView);
        adapter = new SplitAdapter(getContext(), result);
        listView.setAdapter(adapter);

        for (Group g : Objects.requireNonNull(DAO.getInstance().groups.getValue())) {
            if (gid.equals(g.getId())) {
                currentGroup = g;
            }
        }
        assert currentGroup != null;

        expenses = currentGroup.expenses.getValue();
        users = currentGroup.users.getValue();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int N = users.size();
        int M = expenses.size();
        if (N > 1 && M > 0) {
            GFG algorithm = new GFG(N);
            double[] userExpenses = new double[N];
            for (int i = 0; i < N; i++) {
                for (GroupExpenseDTO expense : expenses) {
                    if (expense.paidBy.equals(users.get(i))) {
                        userExpenses[i] += Double.parseDouble(expense.cost);
                    }
                }
            }
            algorithm.initializeGraph(userExpenses);
            algorithm.minCashFlow();
        } else {
            Toast.makeText(getContext(), "Not enough users or expenses", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void finish() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }

    private class GFG {     // Algorithm
        int N;              // Number of persons (or vertices in the graph)
        double[][] graph;      // graph[i][j] indicates the amount person i needs to pay person j

        public GFG(int n) {
            N = n;
            graph = new double[N][N];
        }

        // function that returns index of minimum value in arr[]
        int getMin(double[] arr) {
            int minInd = 0;
            for (int i = 1; i < N; i++)
                if (arr[i] < arr[minInd])
                    minInd = i;
            return minInd;
        }

        // function that returns index of maximum value in arr[]
        int getMax(double[] arr) {
            int maxInd = 0;
            for (int i = 1; i < N; i++)
                if (arr[i] >= arr[maxInd])
                    maxInd = i;
            return maxInd;
        }

        double minOf2(double x, double y) {
            return Math.min(x, y);
        }

        void minCashFlowRec(double[] amount) {
            int mxCredit = getMax(amount), mxDebit = getMin(amount);
            // if amounts are settled -> break recursive chain
            if (amount[mxCredit] < 1.0 && amount[mxDebit] < 1.0) {
                adapter.notifyDataSetChanged();
                return;
            }
            // Find the minimum of two amounts
            double min = minOf2(-amount[mxDebit], amount[mxCredit]);
            amount[mxCredit] -= min;
            amount[mxDebit] += min;
            // RESULT FOUND
            String strA = DAO.getInstance().getUserFromId(users.get(mxDebit)).userData.name;
            String strB = DAO.getInstance().getUserFromId(users.get(mxCredit)).userData.name;
            result.add(new SplitObject(min, strA, strB));
            // continue
            minCashFlowRec(amount);
        }

        void minCashFlow() {
            double[] amount = new double[N];
            for (int p = 0; p < N; p++)
                for (int i = 0; i < N; i++)
                    amount[p] += (graph[i][p] - graph[p][i]);
            minCashFlowRec(amount);
        }

        public void initializeGraph(double[] usersShares) {
            assert N > 1;
            for (int i = 0; i < N; i++) {
                double shares = usersShares[i] / (double) (N - 1);
                for (int j = 0; j < N; j++) {
                    if (j != i) {
                        graph[j][i] += shares;
                    }
                }
            }
        }
    }

    private class SplitObject {
        double value;
        String userAName;
        String userBName;

        public SplitObject(double value, String userAName, String userBName) {
            this.value = value;
            this.userAName = userAName;
            this.userBName = userBName;
        }
    }

    private class SplitAdapter extends ArrayAdapter<SplitObject> {
        public SplitAdapter(Context context, ArrayList<SplitObject> arrayList) {
            super(context, 0, arrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            SplitObject splitObject = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_row_split, parent, false);
            }
            TextView userA = convertView.findViewById(R.id.split_userA);
            TextView userB = convertView.findViewById(R.id.split_userB);
            TextView value = convertView.findViewById(R.id.split_value);
            String valueStr = String.format(Locale.US, "%.2f", splitObject.value);

            userA.setText(splitObject.userAName);
            userB.setText(splitObject.userBName);
            value.setText(valueStr);

            return convertView;
        }
    }
}