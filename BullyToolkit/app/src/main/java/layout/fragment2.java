package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.exetxstate.bullytoolkit.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class fragment2 extends Fragment implements AdapterView.OnItemSelectedListener {

    static int saveDeletePosition;
    View myView; // myView is what gets inflated.
    String closingSort;
    int oldToNewCount = 0;
    int newToOldCount = 0;
    final static String sortFileName = "sortedFile";
    final static String savedSelectionsFile = "selectionsFile";
    String lastSortFileName = "lastSort";
    FileInputStream fin;
    FileOutputStream fos;
    BufferedReader buffReader;
    InputStreamReader inputReader;
    public static ArrayList<String> newList = new ArrayList<>();
    ArrayList<String> tempList = new ArrayList<>();
    String message;
    public static ListView saveListView;
    // ArrayAdapter<String> adapter;  Old non-custom adapter
    public static MyCustomAdapter newAdapter;
    Spinner sortSpinner;
    int saveSortPos;
    Button clearButton;
    String[] sortArray;
    String one = String.valueOf(1);
    String zero = String.valueOf(0);
    static SpinnerSelections selectionsList = new SpinnerSelections();
    private AdView mAdView; // Adview variable for ad banner

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: fragment 2");

        // The string "newList" is actually a key. In MainActivity, when sending data to here, it sends out a key called newList.
        // We search for that key.


        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_fragment2, container, false);

        MobileAds.initialize(getActivity(), "ca-app-pub-7704357348891728/9393908292");
        mAdView = (AdView) myView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//            ArrayList<String> newList = getArguments().getStringArrayList("newList");


        init(myView);
        clearData();
        // readFromFile(lastSortFileName);
        readFromFile(sortFileName);
        readFromFile(savedSelectionsFile);
        Log.d(TAG, "NewList: " + newList);
        if(!selectionsList.bodyType.isEmpty()) {
            newAdapter = new MyCustomAdapter(getContext(), newList, selectionsList);
            saveListView.setAdapter(newAdapter);
        }
       // Log.d(TAG, "onCreateView: " + newAdapter.getCount());
        View testView = inflater.inflate(R.layout.fragment_fragment2, container, false);
        Log.d(TAG, "SDP: " + saveDeletePosition);
        // can also do View.inflate() apparently
        //listViewOnItemClickListener();
        // Passing in context to constructor of new CustomAdapter Object
        // And setting the adapter so that the listView will display.

        //Log.d(TAG, "Adapter count: " + adapter.getCount());

        return myView;

    }


    public void readFromFile(String fileName) {
        int newListSize = newList.size();
        int selectionListSize = selectionsList.bodyType.size(); // could be any
        Log.d(TAG, "In readFromFile Fragment2  selectionsSize  " + selectionsList.height.size());
        Log.d(TAG, "In readFromFile Fragment2  selectionsSize  " + newListSize);
        File file = new File(fileName);
        StringBuffer stringBuff;
        stringBuff = new StringBuffer();
        try {
            fin = getActivity().openFileInput(fileName);
            inputReader = new InputStreamReader(fin);
            buffReader = new BufferedReader(inputReader);
            stringBuff = new StringBuffer();
            int i = 0;
            int j = 0;
            while ((message = buffReader.readLine()) != null) {
               // Log.d(TAG, "readFromFile: after message == bufferreader" + newListSize + selectionListSize);
                // stringBuff.append(message); // Might not need this line
                if (fileName == sortFileName && newListSize <= i) {
                    Log.d(TAG, "readFromFile: inside newList try");
                    newList.add(i, message);
                    i++;
                  //  Log.d(TAG, "making newList");
                } else if (fileName == sortFileName)
                    i++; // increment i when newListSize is not <= i.. so that it skips already read in strings
                if (fileName == savedSelectionsFile && selectionListSize <= i) { // could be any variable of that class to check for null.
                    Log.d(TAG, "readFromFile: selections try");
                    switch (j) {
                        case 0:
                            selectionsList.bodyType.add(i, message);
                            Log.d(TAG, "reading in selections");
                            j++;
                            break;
                        case 1:
                            selectionsList.intelligence.add(i, message);
                            Log.d(TAG, "reading in selections");
                            j++;
                            break;
                        case 2:
                            selectionsList.wealth.add(i, message);
                            Log.d(TAG, "reading in selections");
                            j++;
                            break;
                        case 3:
                            selectionsList.height.add(i, message);
                            Log.d(TAG, "is height??");
                            j = 0;
                            i++;
                            break;
                    }
                } else if (fileName == savedSelectionsFile){  // This is to make sure it doesn't read in too many things.
                    j++;  // skips over the 4 different selections till j==4 then increment i till selectionsList <= i.
                    if(j == 4) {
                        i++;
                        j = 0;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // readFromFile(lastSortFileName);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sortSpinner.setOnItemSelectedListener(this);  // put this here so toast won't popup in mainActivity
            readFromFile(sortFileName);
            readFromFile(savedSelectionsFile);
            Log.d(TAG, "setUserVisibleHint: " + newList);
            Log.d(TAG, "setUserVisibleHint: " + selectionsList.bodyType);
            if(!selectionsList.bodyType.isEmpty()) {  // Temp test
                newAdapter = new MyCustomAdapter(getContext(), newList, selectionsList);
                saveListView.setAdapter(newAdapter);
            }
        }
//        int beforeListSize; // size before user saved any sentences on Fragment 1
//        int afterListSize; // size after user saved sentences on Fragment 1
//        if (isVisibleToUser) {
//            switch(saveSortPos){
//                case 0:
//                    Log.d(TAG, "setUserVisibleHint: case0");
//                    readFromFile(sortFileName);
//                    Log.d(TAG, "after sortFileName");
//                    readFromFile(savedSelectionsFile);
//                    Log.d(TAG, "afterSavedSelectionsFIle");
//                    newAdapter = new MyCustomAdapter(getContext(), newList, selectionsList);
//                    saveListView.setAdapter(newAdapter);
//                    break;
//                case 1:
//                    // Case 1: Adds values from newList to TempList Figures out what the size of the list before the user saves more sentences
//                    // and after. Uses that to loop and assign the new sentences to the beginning of a tempList. It's sorted correctly now.
//                    // clear the old values out of newList .. assign the newly sorted and added values from temp to newList. NewList updates adapter
//                    // automatically because the newList is inside the adapter. The Adapters Textview points at newList. Lastly, setAdapter with the
//                    // updated adapter.
//                    Log.d(TAG, "setUserVisibleHint: case1");
//                    beforeListSize = newList.size();
//                    tempList.addAll(newList);
//                    readFromFile(sortFileName);
//                    readFromFile(savedSelectionsFile);
//                    Log.d(TAG, "newList: " + newList + "tempList: " + tempList );
//                    afterListSize = newList.size();
//                    for(int i = beforeListSize; i < afterListSize; i++){
//                        tempList.add(0, newList.get(i));
//                    }
//                    newList.clear();
//                    newList.addAll(tempList);
//                    tempList.clear();
//                    saveListView.setAdapter(newAdapter);
//                    break;
//                case 2:
//                    Log.d(TAG, "setUserVisibleHint: case3");
//                    readFromFile(sortFileName);
//                    readFromFile(savedSelectionsFile);
//                    saveListView.setAdapter(newAdapter);
//                    break;
//            }
//        } else {
//            // nothing
//        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        Toast myToast = new Toast(getContext());
        myToast.makeText(getContext(), "Sorting coming in version 2. Donate to help :)", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemSelected:");
        // ** Fixing Bugs in Sorting ** //
//        switch (adapterView.getId()) {
//            case R.id.sortingSpinner:
//                saveSortPos = pos;
//                if (!newList.isEmpty()) {
//                    switch (pos) {
//                        case 0:
//                            if (oldToNewCount == 0 && !closingSort.equals("oldToNew") || closingSort.equals("not set")) {
//                                oldToNewCount++;
//                                newToOldCount = 0;
//                                sortSaved();
//                                writeToFile("oldToNew", lastSortFileName, true);
//                            }
//                            break;
//                        case 1:
//                            if (newToOldCount == 0) {
//                                newToOldCount++;
//                                oldToNewCount = 0;
//                                sortSaved();
//                                writeToFile("newToOld", lastSortFileName, true);
//                            }
//                            break;
//                        case 2:
//                            alphabetical();
//                            break;
//                    }
//                    Log.d(TAG, "onItemSelected: " + pos);
//                    break;
//                }
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void clearData() {
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().deleteFile(sortFileName);
                newList.clear();
                getContext().deleteFile(savedSelectionsFile);
                selectionsList.bodyType.clear();
                selectionsList.intelligence.clear();
                selectionsList.wealth.clear();
                selectionsList.height.clear();
                Log.d(TAG, "onClick: " + selectionsList.height.size());
                newAdapter = new MyCustomAdapter(getContext(), newList, selectionsList);
                saveListView.setAdapter(newAdapter);
                //saveListView.setAdapter(newAdapter);
            }
        });
    }


    public void sortSaved() {  // Might bring it in V2.
        // adapter.clear();
        Log.d(TAG, "newList" + newList);
        // Clears both the adapters TextView and the newList StringArrayList.
        // readFromFile(); // Reads whatever is currently in the text file
        for (int i = newList.size(); i > 0; i--) {  // loop makes a re-ordered tempList
            tempList.add(newList.get(i - 1));
            Log.d(TAG, "newToOld:");
        }
        getContext().deleteFile(sortFileName);  // Deletes file, so we can then update file. (make new)
        for (int i = 0; i < newList.size(); i++) {
            writeToFile(tempList.get(i), sortFileName);
            Log.d(TAG, "newToOld: Writing to file");
        }
        newList.clear(); // clears all data in newList
        newList.addAll(tempList); // Do this because newList = tempList is just a reference. tempList.clear() also clears newList
        // by adding the new re-ordered list, we the adapter automatically updates.. The adapter points at the
        saveListView.setAdapter(newAdapter);
        tempList.clear();
    }

    public void alphabetical() {  // Essentially bubble sort with characters
        int sizeOfNewList = newList.size();

        Boolean swaps = true;
        String tempString;
        int minNumCharacters;
        for (int i = 0; i < sizeOfNewList && swaps == true; i++) { // Keep making passes till there are no swaps
            swaps = false;
            Log.d(TAG, "outermost loop, new passthrough");
            for (int j = 0; j < sizeOfNewList - 1; j++) { // Loops through the sentences in the newList StringArrayList
                Log.d(TAG, "second outermost loop, next pair of sentences");
                if (newList.get(j + 1).length() < newList.get(j).length()) {
                    minNumCharacters = newList.get(j + 1).length();
                } else {
                    minNumCharacters = newList.get(j).length();
                }
                Boolean foundMin = false;
                if (newList.get(j + 1).length() != newList.get(j).length()) {  // Need to change this to just check if string is equal... it works more.
                    for (int k = 0, k2 = 0; k < minNumCharacters && k2 < minNumCharacters && foundMin == false; k++, k2++) { // Loops till a minimum character is found. Unless sentences are equal.
                        // I need to add checks for apostrophes and whatnot. ASCII for A is 65. anything below needs to get ignored. 91-96 needs to get ignored
//                           if(newList.get(j).charAt(k) < 65 || (newList.get(j).charAt(k) <= 96 && newList.get(j).charAt(k) >= 91)) k++; // Checks to see if char is a symbol. If it is.. proceed to next character;
//                           if(newList.get(j+1).charAt(k2) < 65 || (newList.get(j+1).charAt(k2) <= 96 && newList.get(j+1).charAt(k2) >= 91)) k2++;  [STILL BUGGY]
                        if (((int) newList.get(j).charAt(k)) > ((int) newList.get(j + 1).charAt(k2))) { // Swapping when find first charcater that is less ASCII value
                            tempString = newList.get(j);
                            newList.set(j, newList.get(j + 1));
                            newList.set(j + 1, tempString);
                            foundMin = true;
                            swaps = false;
                            Log.d(TAG, "newList: " + newList);
                        } else if (((int) newList.get(j).charAt(k)) < ((int) newList.get(j + 1).charAt(k2))) { // Swapping when find first charcater that is less ASCII value
                            foundMin = true;
                            swaps = true;
                            Log.d(TAG, "newList: " + newList);
                        }
                    }
                }
            }
        }
        Log.d(TAG, "newList Final: " + newList);
        saveListView.setAdapter(newAdapter);
        // I would have to write to another file to save an alphabetized list and still keep track of new and old.
        // alphabetize is only temporary till you close the application.
    }

    public void writeToFile(String sentence, String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                //fos = new FileOutputStream(fosTest, true);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
            }
            fos.write(sentence.getBytes());
            fos.write("\n".getBytes());
            fos.close();
            Log.d(TAG, getActivity().getFilesDir().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String sortType, String fileName, Boolean wipeData) {
        Log.d(TAG, "writeToFile: ");
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                //fos = new FileOutputStream(fosTest, true);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                Log.d(TAG, "creating new file for logging");
            } else if (wipeData) {
                getContext().deleteFile(fileName);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                Log.d(TAG, "inside wipe");

            }

            fos.write(sortType.getBytes());
            // Don't need in this one: fos.write("\n".getBytes());
            fos.close();
            Log.d(TAG, getActivity().getFilesDir().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(View myView) {  // Initialize variables
        clearButton = (Button) myView.findViewById(R.id.clearButton);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bully.otf");
        clearButton.setTypeface(tf);
        clearButton.setTransformationMethod(null);

        sortSpinner = (Spinner) myView.findViewById(R.id.sortingSpinner);
        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sortArray, R.layout.spinnerlayoutcustom);
        sortSpinner.setAdapter(sortAdapter);


        ViewGroup.LayoutParams buttonParams = clearButton.getLayoutParams(); // Make new instance of ViewGroup.LayoutParams Inititate to button layoutparams
        buttonParams.width = sortSpinner.getWidth();   // set width to sort spinners width
        clearButton.setLayoutParams(buttonParams);  // set the params to the new instance of ViewGroup,
        // readFromFile(sortFileName);
        saveListView = (ListView) myView.findViewById(R.id.saveListView);


        // adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, newList); For Non Custom ListViews
    }


    public static void deleteRow(int position, Context context) {  // Deletes Row. Made sep. function for readability
        Log.d(TAG, "position: " + position);
        newList.remove(position);
        selectionsList.bodyType.remove(position);
        selectionsList.intelligence.remove(position);
        selectionsList.wealth.remove(position);
        selectionsList.height.remove(position);
        context.deleteFile(sortFileName);
        context.deleteFile(savedSelectionsFile);
        Log.d(TAG, "testoutsideTry");
        try {
            FileOutputStream fos = context.openFileOutput(sortFileName, Context.MODE_APPEND);
            for(int i = 0; i < newList.size(); i++){
                fos.write(newList.get(i).getBytes());
                fos.write("\n".getBytes());
            }
            fos = context.openFileOutput(savedSelectionsFile, Context.MODE_APPEND);
            for(int i = 0; i < selectionsList.bodyType.size(); i++){  // could be any of the member variable sizes
                for(int j = 0; j < 4; j++) { // 4 is how many selection filters there are.
                    switch(j){
                        case 0: fos.write(selectionsList.bodyType.get(i).getBytes());
                            Log.d(TAG, "test" + selectionsList.bodyType.size());
                            fos.write("\n".getBytes());
                            break;
                        case 1:fos.write(selectionsList.intelligence.get(i).getBytes());
                            Log.d(TAG, "test" + selectionsList.intelligence.size());
                            fos.write("\n".getBytes());
                            break;
                        case 2:fos.write(selectionsList.wealth.get(i).getBytes());
                            Log.d(TAG, "test" + selectionsList.wealth.size());
                            fos.write("\n".getBytes());
                            break;
                        case 3:fos.write(selectionsList.height.get(i).getBytes());
                            Log.d(TAG, "test" + selectionsList.height.size());
                            fos.write("\n".getBytes());
                            break;
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "after try");
        newAdapter = new MyCustomAdapter(context, newList, selectionsList);
        saveListView.setAdapter(newAdapter);
    }
}
