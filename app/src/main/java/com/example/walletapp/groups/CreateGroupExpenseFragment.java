package com.example.walletapp.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.walletapp.DAO;
import com.example.walletapp.DBS;
import com.example.walletapp.GroupExpenseDTO;
import com.example.walletapp.R;
import com.example.walletapp.expense.CreateExpenseActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CreateGroupExpenseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String uid;
    private String gid;

    private ImageView btn_back;
    private TextInputEditText et_title, et_cost, et_description;
    private AutoCompleteTextView atv_category, atv_paidBy, atv_splitMethod;
    private Button btn_create;

    private final ArrayList<String> categoriesList = new ArrayList<>();
    private final ArrayList<String> splitMethodList = new ArrayList<>();
    private final ArrayList<GroupUser> paidByList = new ArrayList<>();
    private GroupUser lastPaidByUser = null;


    public CreateGroupExpenseFragment() {
        // Required empty public constructor
    }

    public static CreateGroupExpenseFragment newInstance(String uid, String gid) {
        CreateGroupExpenseFragment fragment = new CreateGroupExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, uid);
        args.putString(ARG_PARAM2, gid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
            gid = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group_expense, container, false);

        btn_back = view.findViewById(R.id.grExp_btn_back);
        et_title = view.findViewById(R.id.grExp_et_titleCREATE);
        et_cost = view.findViewById(R.id.grExp_et_costCREATE);
        et_description = view.findViewById(R.id.grExp_et_descCREATE);
        atv_category = view.findViewById(R.id.grExp_spinner_category);
        atv_paidBy = view.findViewById(R.id.grExp_spinner_paidBy);
        atv_splitMethod = view.findViewById(R.id.grExp_spinner_splitMethod);
        btn_create = view.findViewById(R.id.grExp_btn_addCREATE);

        categoriesList.addAll(DBS.categoriesList);
        paidByList.addAll(DAO.getInstance().getGroupUsersList(gid));
        splitMethodList.addAll(DBS.splitMethodList);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item_category, categoriesList);
        atv_category.setAdapter(categoryAdapter);
        ArrayAdapter<String> splitMethodAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item_category, splitMethodList);
        atv_splitMethod.setAdapter(splitMethodAdapter);
        ArrayAdapter<GroupUser> paidByAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item_category, paidByList);
        atv_paidBy.setAdapter(paidByAdapter);

        String defaultPaidBy = "Me";
        atv_paidBy.setText(defaultPaidBy, false);
        atv_category.setText(categoriesList.get(0), false);
        atv_splitMethod.setText(splitMethodList.get(0), false);

        btn_create.setOnClickListener(click -> {
            String title = et_title.getText().toString();
            String cost = et_cost.getText().toString();
            String category = atv_category.getText().toString();
            String description = et_description.getText().toString();
            String splitMethod = atv_splitMethod.getText().toString();
            String paidBy;
            if(lastPaidByUser == null)
                paidBy = uid;
            else if(atv_paidBy.getText().toString().equals(lastPaidByUser.name))
                paidBy = lastPaidByUser.uid;
            else throw new RuntimeException("Unexpected paidBy state! <-- CreateGroupExpenseFragment");

            GroupExpenseDTO expenseDTO = new GroupExpenseDTO(title, cost, category, paidBy, splitMethod, description);
            Toast.makeText(getContext(), expenseDTO.toString(), Toast.LENGTH_LONG).show();

            //validate
            //create object
            //send to db
            //close fragment
        });


        btn_back.setOnClickListener(c -> finish());

        atv_paidBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastPaidByUser = (GroupUser) parent.getItemAtPosition(position);
            }
        });

        return view;
    }

    private void finish() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }

}