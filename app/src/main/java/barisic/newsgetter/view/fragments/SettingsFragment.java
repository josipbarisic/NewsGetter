package barisic.newsgetter.view.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import barisic.newsgetter.R;
import barisic.newsgetter.view.activities.MainActivity;
import barisic.newsgetter.model.db_classes.Source;
import barisic.newsgetter.view.adapters.SourcesRecyclerViewAdapter;
import barisic.newsgetter.viewmodel.helper_classes.AlertBuilder;
import barisic.newsgetter.viewmodel.db_classes.SourceViewModel;

public class SettingsFragment extends Fragment {

    private Button btnConfirm;
    private Button btnSelectUnselectAll;

    private ArrayList<String> namesList = new ArrayList<>();
    private ArrayList<String> domainsList = new ArrayList<>();
    private ArrayList<String> urlsList = new ArrayList<>();
    private ArrayList<Integer> selectedSources = new ArrayList<>();

    private SourceViewModel viewModel;

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnConfirm = view.findViewById(R.id.btnSelectSources);
        btnSelectUnselectAll = view.findViewById(R.id.btnSelectUnselectAll);

        viewModel = ViewModelProviders.of(this).get(SourceViewModel.class);

        getSources(view);

        return view;
    }

    private void getSources(final View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbSources = database.getReference("sources");
        dbSources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    namesList.add(String.valueOf(snap.child("name").getValue()));
                    domainsList.add(String.valueOf(snap.child("domain").getValue()));
                    urlsList.add(String.valueOf(snap.child("url").getValue()));
                }

                initRecyclerView(view, namesList, domainsList, urlsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView(View view, ArrayList<String> names, ArrayList<String> domains, ArrayList<String> urls){
        RecyclerView recyclerView = view.findViewById(R.id.sources_recycler_view);
        final SourcesRecyclerViewAdapter adapter = new SourcesRecyclerViewAdapter(names, domains, urls, viewModel.getAllSources());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter.checkSelectedSources(viewModel.getAllSources());

        if(viewModel.getAllSources().size() == adapter.getItemCount()){
            btnSelectUnselectAll.setText(R.string.unselect_all_text);
        }

        if(adapter.getSelectedPositions().isEmpty()){
            AlertBuilder.newDialog(getContext(), R.string.no_sources_warning);
        }

        btnSelectUnselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSources = adapter.getSelectedPositions();
                if(btnSelectUnselectAll.getText().equals(getString(R.string.select_all_text))){
                    adapter.setSelectedPositions("all");
                    btnSelectUnselectAll.setText(R.string.unselect_all_text);
            }
                else{
                    adapter.setSelectedPositions("none");
                    btnSelectUnselectAll.setText(R.string.select_all_text);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);


                viewModel.deleteAllSources();
                Log.d("TAAG", "onClick: " + adapter.getSelectedPositions());

                selectedSources = adapter.getSelectedPositions();

                if(selectedSources != null && !selectedSources.isEmpty()){
                    for(int i = 0; i < selectedSources.size(); i++){
                        Log.d("SELECTED", "onClick: " + selectedSources.get(i) + " " + namesList.get(selectedSources.get(i)) + " DOMAIN: " + domainsList.get(selectedSources.get(i)));
                        viewModel.insertSource(new Source(namesList.get(selectedSources.get(i)), domainsList.get(selectedSources.get(i))));
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(intent);
                }
                else{
                    AlertBuilder.newDialog(getContext(), R.string.no_sources_warning);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
