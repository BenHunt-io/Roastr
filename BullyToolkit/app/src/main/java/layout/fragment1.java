package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.exetxstate.bullytoolkit.MainActivity;
import com.exetxstate.bullytoolkit.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextView;

import static android.content.ContentValues.TAG;


public class fragment1 extends Fragment implements AdapterView.OnItemSelectedListener {

    String[] sentenceJunction = {" and ", " also ", " not to mention ", " and to top it off "};


    RadioButton clean; // RadioButton converted to a "Switch" for clean/dirty filter
    RadioButton dirty; // RadioButton converted to a "Switch" for clean/dirty filter

    FileOutputStream fos;

    String sentence; // ▼ Used for undo/redo
    String lastSentence;

    int generateCounter = 0; // counts how many times generate is clicked.. idk why

    ImageButton soundButton; // textToVoice button
    TextToSpeech ttsObject; // textToSpeech object
    ImageButton undoButton; // undo/redo button

    int setLangResult;

    int saveIntelligencePos; // used to save position of intelligence filter
    int saveBodyPos; // used to save position of body filter
    int saveWealthPos; // used to save position of wealth filter
    int saveHeightPos; // used to save position of height filter

    Button generateButton; // generates
    Button saveButton; // saves

    public SpinnerSelections spinnerList = new SpinnerSelections(); // class that holds lists
    // of all the different filters.
    Spinner bodyTypeSpinner; // 4 spinners (filters) ▼
    Spinner intelligenceSpinner;
    Spinner wealthSpinner;
    Spinner heightSpinner;


    TextView displayGenerate; // sentence (roast) that is displayed to user

    TextView fileInputTest;
    GifImageView bullygif; // loading animation gif

    String sortFileName = "sortedFile";
    String savedSelectionsFile = "selectionsFile";

    Button submitButton; // Button for submitting user roasts to FireBase Database
    EditText customRoastField; // Text Field for entering user created custom roast
    CheckBox submitKind; // What kind of roast - Clean / Dirty

    private AdView mAdView; // Adview variable for ad banner


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment1, container, false);

        clean = (RadioButton)myView.findViewById(R.id.clean); // clean filter
        dirty = (RadioButton)myView.findViewById(R.id.dirty); // dirty filter
        dirty.setChecked(true); // dirty by default


        bullygif = (GifImageView) myView.findViewById(R.id.bullygif);
        bullygif.setVisibility(bullygif.INVISIBLE); // Set invisible at start

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bully.otf");


        writeToDatabase(myView, tf); // Waits on Button to be clicked. Takes in inflated layout VIEW
        runAds(myView); // runs function that begins ads
        initializeSpinners(myView);
        generate(myView, tf);
        save(myView, tf);
        undo(myView);
        textToVoice(myView);


        fileInputTest = (TextView) getActivity().findViewById(R.id.fileInputTest);


        submitButton.setTransformationMethod(null); // Disables buttons from being sets to ALL CAPS
        generateButton.setTransformationMethod(null); // Disables buttons from being sets to ALL CAPS
        saveButton.setTransformationMethod(null); // Disables buttons from being sets to ALL CAPS

        return myView;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // raceSpinner.getItemAtPosition(pos).toString(); <-- this returns the name of the spinner selected at a position

    ///////////////////////////////////////////////////////////////////////////////////////////*
    //OnItemSelected(--,--,--,--): Depending on what spinner is selected it saves the integer
    //position of that selection. IE unfit pos = 0
    //Returns: [Void]
    ///////////////////////////////////////////////////////////////////////////////////////////*
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (adapterView.getId()) {
            case R.id.intelligenceSpinner:
                saveIntelligencePos = pos;
                break;
            case R.id.bodySpinner:
                saveBodyPos = pos;
                break;
            case R.id.wealthSpinner:
                saveWealthPos = pos;
                break;
            case R.id.heightSpinner:
                saveHeightPos = pos;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    ///////////////////////////////////////////////////////////////////////////////////*
    // makeSentence(RadioButton clean, RadioButton dirty): Takes in two RadioButtons
    // because to set default checked to dirty, initialization must be before makeSentence
    // Depending on the selections
    // from the spinners a sentence is created from the FilteredClass that holds all
    // the different sentences/roasts a random number is generated to make random
    // selection of sentences within the selected filters
    // Finds random int at each level of the array. Strings stored in FilteredClass.java
    // Returns: [String] - Sentence/Roast to be displayed
    ///////////////////////////////////////////////////////////////////////////////////*
    public String makeSentence() {

        Boolean cleanChecked;

        if(clean.isChecked())
            cleanChecked = true;
        else
            cleanChecked = false;


        final int four = 4;
        int position1; // Randomly checks position of 1 of 4 filters selected
        int position2; // Randomly checks position of 1 of 4 filters selected
        String sentenceOne;
        String sentenceTwo;
        String fullSentence = new String();
        Random rand = new Random();
        FilteredClass sentObj = new FilteredClass();

        if (!cleanChecked) { // Find random at each level of the array. Strings stored in FilteredClass.java
            //  ▼  random number to select either clean or dirty (dirty checked means either)
            int randCleanDirty = rand.nextInt(sentObj.allSelections.length);
            //  ▼  random number to select 1 of our 4 filters
            int randTwoOfFour = rand.nextInt(sentObj.allSelections[randCleanDirty].length);
            switch (randTwoOfFour) { // Makes sure it chooses right user selected filter
                case 0:
                    position1 = saveBodyPos;
                    break;
                case 1:
                    position1 = saveIntelligencePos;
                    break;
                case 2:
                    position1 = saveWealthPos;
                    break;
                case 3:
                    position1 = saveHeightPos;
                    break;
                default:
                    position1 = -1;
            }

            int randSentence = rand.nextInt(sentObj.allSelections[randCleanDirty][randTwoOfFour][position1].length);
            sentenceOne = sentObj.allSelections[randCleanDirty][randTwoOfFour][position1][randSentence];
            int randCleanDirty2 = rand.nextInt(sentObj.allSelections.length);
            int randTwoOfFour2 = rand.nextInt(sentObj.allSelections[randCleanDirty2].length);
            switch (randTwoOfFour2) {
                case 0:
                    position2 = saveBodyPos;
                    break;
                case 1:
                    position2 = saveIntelligencePos;
                    break;
                case 2:
                    position2 = saveWealthPos;
                    break;
                case 3:
                    position2 = saveHeightPos;
                    break;
                default:
                    position2 = -1;
            }
            Log.d(TAG, "pos1" + randCleanDirty2 + " " + randTwoOfFour2 + " " + position2);
            int randSentence2 = rand.nextInt(sentObj.allSelections[randCleanDirty2][randTwoOfFour2][position2].length);
            ;
            sentenceTwo = sentObj.allSelections[randCleanDirty2][randTwoOfFour2][position2][randSentence2];
            // Don't need +0 because its from 0 to length exclusive by default
            int randJunction = rand.nextInt(sentenceJunction.length);
            char[] newSentence = sentenceTwo.toCharArray(); // next 3 lines converts first letter 2nd sentence to lowercase
            char lowerCase = sentenceTwo.toLowerCase().charAt(0);
            newSentence[0] = lowerCase;
            sentenceTwo = new String(newSentence);
            fullSentence = sentenceOne + sentenceJunction[randJunction] + sentenceTwo;
        }
        else{ // else make a clean roast
            // 1 is clean roasts
            int randTwoOfFour = rand.nextInt(sentObj.allSelections[1].length);
            switch (randTwoOfFour) { // Makes sure it chooses right user selected filter
                case 0:
                    position1 = saveBodyPos;
                    break;
                case 1:
                    position1 = saveIntelligencePos;
                    break;
                case 2:
                    position1 = saveWealthPos;
                    break;
                case 3:
                    position1 = saveHeightPos;
                    break;
                default:
                    position1 = -1;
            }

            int randSentence = rand.nextInt(sentObj.allSelections[1][randTwoOfFour][position1].length);
            sentenceOne = sentObj.allSelections[1][randTwoOfFour][position1][randSentence];
            int randTwoOfFour2 = rand.nextInt(sentObj.allSelections[1].length);
            switch (randTwoOfFour2) {
                case 0:
                    position2 = saveBodyPos;
                    break;
                case 1:
                    position2 = saveIntelligencePos;
                    break;
                case 2:
                    position2 = saveWealthPos;
                    break;
                case 3:
                    position2 = saveHeightPos;
                    break;
                default:
                    position2 = -1;
            }
            int randSentence2 = rand.nextInt(sentObj.allSelections[1][randTwoOfFour2][position2].length);
            sentenceTwo = sentObj.allSelections[1][randTwoOfFour2][position2][randSentence2];
            // Don't need +0 because its from 0 to length exclusive by default
            int randJunction = rand.nextInt(sentenceJunction.length);
            char[] newSentence = sentenceTwo.toCharArray(); // next 3 lines converts first letter 2nd sentence to lowercase
            char lowerCase = sentenceTwo.toLowerCase().charAt(0);
            newSentence[0] = lowerCase;
            sentenceTwo = new String(newSentence);
            fullSentence = sentenceOne + sentenceJunction[randJunction] + sentenceTwo;

        }

        generateCounter++;
        return fullSentence;
    }

    ///////////////////////////////////////////////////////////////////////////////////*
    // writeToFile(String sentence, String FileName): Takes in inflated view from the onCreateView()
    //
    //
    // Returns: Void
    ///////////////////////////////////////////////////////////////////////////////////*
    public void writeToFile(String sentence, String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                //fos = new FileOutputStream(fosTest, true);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                // If file does not exist, means saved has been cleared. So saved selections should clear too.
                // Maybe a better way to do this.. Fragment 2?
                if (spinnerList.bodyType != null) spinnerList.bodyType.clear();
                if (spinnerList.intelligence != null) spinnerList.intelligence.clear();
                if (spinnerList.wealth != null) spinnerList.wealth.clear();
                if (spinnerList.height != null) spinnerList.height.clear();
//                if(spinnerList != null) {
//                    spinnerList.bodyType.clear();
//                    spinnerList.intelligence.clear();
//                    spinnerList.wealth.clear();
//                    spinnerList.height.clear();
//                }
            }
            fos.write(sentence.getBytes());
            fos.write("\n".getBytes());
            fos.close();
            //  Log.d(TAG, getActivity().getFilesDir().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////*
    // writeToFile(String fileName): Takes in inflated view from the onCreateView()
    //
    //
    // Returns: Void
    ///////////////////////////////////////////////////////////////////////////////////*
    public void writeToFile(String fileName) {
        // Saves users spinner selections in strings for readability in writing to file
        String bodyTypeSelection = bodyTypeSpinner.getItemAtPosition(saveBodyPos).toString();
        String intelligenceSelection = intelligenceSpinner.getItemAtPosition(saveIntelligencePos).toString();
        String wealthSelection = wealthSpinner.getItemAtPosition(saveWealthPos).toString();
        String heightSelection = heightSpinner.getItemAtPosition(saveHeightPos).toString();
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                //fos = new FileOutputStream(fosTest, true);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                // If file does not exist, means saved has been cleared. So saved selections should clear too.
                // Maybe a better way to do this.. Fragment 2?
                if (spinnerList.bodyType != null) spinnerList.bodyType.clear();
                if (spinnerList.intelligence != null) spinnerList.intelligence.clear();
                if (spinnerList.wealth != null) spinnerList.wealth.clear();
                if (spinnerList.height != null) spinnerList.height.clear();
            }
            Log.d(TAG, "writeToFile: " + bodyTypeSpinner.getItemAtPosition(saveBodyPos).toString());
            Log.d(TAG, "writeToFile: " + intelligenceSpinner.getItemAtPosition(saveIntelligencePos).toString());
            Log.d(TAG, "writeToFile: " + wealthSpinner.getItemAtPosition(saveWealthPos).toString());
            Log.d(TAG, "writeToFile: " + heightSpinner.getItemAtPosition(saveHeightPos).toString());
            fos.write(bodyTypeSelection.getBytes());
            fos.write("\n".getBytes());
            fos.write(intelligenceSelection.getBytes());
            fos.write("\n".getBytes());
            fos.write(wealthSelection.getBytes());
            fos.write("\n".getBytes());
            fos.write(heightSelection.getBytes());
            fos.write("\n".getBytes());
            fos.close();
            //  Log.d(TAG, getActivity().getFilesDir().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // sets values to instance of SpinnerSelections class. (Saves what users selected when they click save)
    }


    ///////////////////////////////////////////////////////////////////////////////////*
    // writeToDatabase(View myView, Typeface tf): Takes in inflated view from the
    // onCreateView() it is called myView and a font, tf. This function writes to the
    // FireBase database without overwriting. Authentication not required due to the
    // nature of the program
    //
    // Returns: Void
    ///////////////////////////////////////////////////////////////////////////////////*
    public void writeToDatabase(View myView, Typeface tf) {
        submitButton = (Button) myView.findViewById(R.id.submitButton);
        customRoastField = (EditText)myView.findViewById(R.id.customRoastField);
        submitKind = (CheckBox)myView.findViewById(R.id.checkBox);

        submitButton.setTypeface(tf);
        customRoastField.setTypeface(tf);
        submitKind.setTypeface(tf);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                // .push() enables for it to not overwrite previous submission to database
                DatabaseReference myRef;

                if(submitKind.isChecked()) {
                    myRef = firebaseDatabase.getReference("Dirty").push();
                    myRef.setValue(customRoastField.getText().toString());
                    // How to toast: Toast toast = Toast.makeText(context,text,duration); then .show()
                    Toast.makeText(getActivity(),"Submission to database successful!\nSubmission under review (24hrs) ", Toast.LENGTH_SHORT).show();
                }
                else {
                    myRef = firebaseDatabase.getReference("Clean").push();
                    myRef.setValue(customRoastField.getText().toString());
                    Toast.makeText(getActivity(),"Submission to database successful!\nSubmission under review (24hrs) ", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////*
    // runAds(View myView): Takes in inflated view from the onCreateView()
    // it is called myView. Makes an ad request and passes it into the adView to load
    //
    // Returns: Void                                                                   *
    ///////////////////////////////////////////////////////////////////////////////////*
    public void runAds(View myView){

        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) myView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void generate(View myView, Typeface tf){
        displayGenerate = (TextView) myView.findViewById(R.id.generateText); // TextField for roasts
        generateButton = (Button) myView.findViewById(R.id.generateButton);
        generateButton.setTypeface(tf);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentence = makeSentence();

                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        try {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    lastSentence = displayGenerate.getText().toString();
                                    displayGenerate.setText("");
                                    bullygif.setVisibility(bullygif.VISIBLE);
                                }
                            });
                            sleep(750);
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    bullygif.setVisibility(bullygif.INVISIBLE);
                                    displayGenerate.setText(sentence);
                                    //newList.add(sentence.toString());
                                    Log.d(TAG, "new Sentence: " + sentence + "Old Sentence: " + lastSentence);
                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                };
                timer.start();
            }
        });
    }

    public void save(View myView, Typeface tf){

        saveButton = (Button) myView.findViewById(R.id.savedButton);
        saveButton.setTypeface(tf);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer saveBuff;
                if (sentence != null) {
                    writeToFile(sentence, sortFileName);
                    writeToFile(savedSelectionsFile);

                }
            }
        });
    }

    public void undo(View myView){
        undoButton = (ImageButton) myView.findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (lastSentence != null) {
                            String tempSentence;
                            displayGenerate.setText(lastSentence);
                            tempSentence = lastSentence;
                            lastSentence = sentence;
                            sentence = tempSentence;
                        }
                        Log.d(TAG, "run: test");
                    }
                });

            }
        });
    }

    public void textToVoice(View myView){
        soundButton = (ImageButton) myView.findViewById(R.id.soundButton);
        ttsObject = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    setLangResult = ttsObject.setLanguage(Locale.ENGLISH);
                }

            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setLangResult == TextToSpeech.LANG_NOT_SUPPORTED || setLangResult == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(getContext(), "Lanuage not supported", Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttsObject.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        ttsObject.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    public void initializeSpinners(View myView){
        wealthSpinner = (Spinner) myView.findViewById(R.id.wealthSpinner);
        ArrayAdapter wealthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.wealthSpinArray, R.layout.spinnerlayoutcustom);
        wealthSpinner.setAdapter(wealthAdapter);
        wealthSpinner.setOnItemSelectedListener(this);

        heightSpinner = (Spinner) myView.findViewById(R.id.heightSpinner);
        ArrayAdapter heightAdapter = ArrayAdapter.createFromResource(getContext(), R.array.heightSpinArray, R.layout.spinnerlayoutcustom);
        heightSpinner.setAdapter(heightAdapter);
        heightSpinner.setOnItemSelectedListener(this);

        bodyTypeSpinner = (Spinner) myView.findViewById(R.id.bodySpinner);  // NEed to change resource to match name
        ArrayAdapter bodyTypeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.bodyTypeSpinArray, R.layout.spinnerlayoutcustom);
        bodyTypeSpinner.setAdapter(bodyTypeAdapter);
        bodyTypeSpinner.setOnItemSelectedListener(this);

        intelligenceSpinner = (Spinner) myView.findViewById(R.id.intelligenceSpinner);  // Need to change resource to match name
        ArrayAdapter intelligenceAdapter = ArrayAdapter.createFromResource(getContext(), R.array.intelligenceSpinArray, R.layout.spinnerlayoutcustom);
        intelligenceSpinner.setAdapter(intelligenceAdapter);
        intelligenceSpinner.setOnItemSelectedListener(this);
    }

}
