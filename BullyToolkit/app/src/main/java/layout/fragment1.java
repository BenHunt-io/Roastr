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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.exetxstate.bullytoolkit.MainActivity;
import com.exetxstate.bullytoolkit.R;

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


public class fragment1 extends Fragment implements AdapterView.OnItemSelectedListener{

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Testing GitHub Commits and Pulls ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    String[] sentenceJunction = {" and ", " also ", " not to mention ", " and to top it off ", " and shit, "};


    TextToSpeech ttsObject;
    ArrayList<String> newList = new ArrayList<>();
    public SpinnerSelections spinnerList = new SpinnerSelections();
    FileOutputStream fos;
    String sentence;
    String lastSentence;
    int generateCounter = 0;
    ImageButton soundButton;
    ImageButton undoButton;
    ArrayAdapter raceAdapter;
    int setLangResult;
    int saveIntelligencePos;
    int saveBodyPos;
    int saveWealthPos;
    int saveHeightPos;
    Button generateButton;
    Button saveButton;
    Spinner bodyTypeSpinner;
    Spinner intelligenceSpinner;
    Spinner wealthSpinner;
    Spinner heightSpinner;
    TextView displayGenerate;
    TextView fileInputTest;
    GifImageView bullygif;
    Intent intent;
    Switch cleanFilter;
    String sortFileName = "sortedFile";
    String savedSelectionsFile = "selectionsFile";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_fragment1, container, false);

        //readFromFile();

        fileInputTest = (TextView)getActivity().findViewById(R.id.fileInputTest);
        soundButton = (ImageButton)myView.findViewById(R.id.soundButton);
        undoButton = (ImageButton)myView.findViewById(R.id.undoButton);
        saveButton = (Button)myView.findViewById(R.id.savedButton);

//        String test = "TESSTT";
//        intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
//        intent.putExtra("newList", test);
//        getActivity().startActivity(intent);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer saveBuff;
                if(sentence != null) {
                    writeToFile(sentence, sortFileName);
                    writeToFile(savedSelectionsFile);
                    
                }
//                int count = 0;

//                intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
//                intent.putStringArrayListExtra("newList", newList);
//                getActivity().startActivity(intent);


//               while(newList.size() > count){
//                   Log.d(TAG, "Size of List: " + newList.size());
//                   Log.d(TAG, newList.get(count) + " TESTSPACE ");
//                   count++;
//               }
            }
        });

        //cleanFilter = (Switch)myView.findViewById(R.id.cleanFilter);
        //cleanFilter.setTrackDrawable(R.drawable.switchdrawable);


        wealthSpinner = (Spinner)myView.findViewById(R.id.wealthSpinner);
        ArrayAdapter wealthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.wealthSpinArray, R.layout.spinnerlayoutcustom);
        wealthSpinner.setAdapter(wealthAdapter);
        wealthSpinner.setOnItemSelectedListener(this);

        heightSpinner = (Spinner)myView.findViewById(R.id.heightSpinner);
        ArrayAdapter heightAdapter = ArrayAdapter.createFromResource(getContext(), R.array.heightSpinArray, R.layout.spinnerlayoutcustom);
        heightSpinner.setAdapter(heightAdapter);
        heightSpinner.setOnItemSelectedListener(this);

        bodyTypeSpinner = (Spinner)myView.findViewById(R.id.bodySpinner);  // NEed to change resource to match name
        ArrayAdapter bodyTypeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.bodyTypeSpinArray, R.layout.spinnerlayoutcustom);
        bodyTypeSpinner.setAdapter(bodyTypeAdapter);
        bodyTypeSpinner.setOnItemSelectedListener(this);

        intelligenceSpinner = (Spinner)myView.findViewById(R.id.intelligenceSpinner);  // Need to change resource to match name
        ArrayAdapter intelligenceAdapter = ArrayAdapter.createFromResource(getContext(), R.array.intelligenceSpinArray, R.layout.spinnerlayoutcustom);
        intelligenceSpinner.setAdapter(intelligenceAdapter);
        intelligenceSpinner.setOnItemSelectedListener(this);

        bullygif = (GifImageView)myView.findViewById(R.id.bullygif);
        bullygif.setVisibility(bullygif.INVISIBLE);
        displayGenerate = (TextView)myView.findViewById(R.id.generateText);
        generateButton = (Button)myView.findViewById(R.id.generateButton);



        generateButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
        sentence = makeSentence();

        Thread timer = new Thread() {
            @Override
            public void run() {
                try{
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

                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }

            }


        };
        timer.start();
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if(lastSentence != null) {
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

        ttsObject = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    setLangResult = ttsObject.setLanguage(Locale.ENGLISH);
                }

            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setLangResult == TextToSpeech.LANG_NOT_SUPPORTED || setLangResult == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(getContext(), "Lanuage not supported", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttsObject.speak(sentence, TextToSpeech.QUEUE_FLUSH, null,null);
                    }
                    else {
                        ttsObject.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });

        generateButton.setTransformationMethod(null); // Disables buttons from being sets to ALL CAPS
        saveButton.setTransformationMethod(null); // Disables buttons from being sets to ALL CAPS
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bully.otf");
        generateButton.setTypeface(tf);
        saveButton.setTypeface(tf);
       // getContext().deleteFile(fosTest);
        return myView;




    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // raceSpinner.getItemAtPosition(pos).toString(); <-- this returns the name of the spinner selected at a position
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        switch(adapterView.getId()){
            case R.id.intelligenceSpinner: saveIntelligencePos = pos;
                break;
            case R.id.bodySpinner: saveBodyPos = pos;
                break;
            case R.id.wealthSpinner: saveWealthPos = pos;
                break;
            case R.id.heightSpinner: saveHeightPos = pos;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    @Override
//    public void onClick(View view) {
//        //View parent = (View)view.getParent();
//        //bullygif = (GifImageView)parent.findViewById(R.id.bullygif);
//        //bullygif.setVisibility(bullygif.INVISIBLE);
//        final String sentence = makeSentence();
//
//        Thread timer = new Thread() {
//            @Override
//            public void run() {
//                try{
//                    sleep(2000);
//
//                }
//                catch(InterruptedException e){
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        };
//        timer.start();
//    }

    public String makeSentence(){
        final int four = 4;
        int position1;
        int position2;
        String sentenceOne;
        String sentenceTwo;
        String fullSentence = new String();
        Boolean clean = false;
        Random rand = new Random();
        FilteredClass sentObj = new FilteredClass();

        if(!clean){ // Find random at each level of the array. Strings stored in FilteredClass.java
            int randCleanDirty = rand.nextInt(sentObj.allSelections.length + 0);
            int randTwoOfFour = rand.nextInt(sentObj.allSelections[randCleanDirty].length + 0);
            switch(randTwoOfFour){ // Makes sure it chooses right user selected filter
                case 0: position1 = saveBodyPos;
                    break;
                case 1: position1 = saveIntelligencePos;
                    break;
                case 2: position1 = saveWealthPos;
                    break;
                case 3: position1 = saveHeightPos;
                    break;
                default: position1 = -1;
            }
            Log.d(TAG, "saveSorts" + saveBodyPos + " " + saveIntelligencePos + " " + saveWealthPos + " " + saveHeightPos );
            int randSentence =  rand.nextInt(sentObj.allSelections[randCleanDirty][randTwoOfFour][position1].length + 0);
            Log.d(TAG, "pos1" + randCleanDirty + " "+ randTwoOfFour + " " + position1 + " " + randSentence);
            sentenceOne = sentObj.allSelections[randCleanDirty][randTwoOfFour][position1][randSentence].toString();

            int randCleanDirty2 = rand.nextInt(sentObj.allSelections.length + 0);
            int randTwoOfFour2 = rand.nextInt(sentObj.allSelections[randCleanDirty2].length + 0);
            switch(randTwoOfFour2){
                case 0: position2 = saveBodyPos;
                    break;
                case 1: position2 = saveIntelligencePos;
                    break;
                case 2: position2 = saveWealthPos;
                    break;
                case 3: position2 = saveHeightPos;
                    break;
                default: position2 = -1;
            }
            Log.d(TAG, "pos1" + randCleanDirty2 + " "+ randTwoOfFour2 + " " + position2);
            int randSentence2 =  rand.nextInt(sentObj.allSelections[randCleanDirty2][randTwoOfFour2][position2].length + 0);;
            sentenceTwo = sentObj.allSelections[randCleanDirty2][randTwoOfFour2][position2][randSentence2].toString();
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

    public void writeToFile(String sentence, String fileName){
        File file = new File(fileName);
        try {
            if(!file.exists()) {
                //fos = new FileOutputStream(fosTest, true);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND );
                // If file does not exist, means saved has been cleared. So saved selections should clear too.
                // Maybe a better way to do this.. Fragment 2?
                if(spinnerList.bodyType != null) spinnerList.bodyType.clear();
                if(spinnerList.intelligence != null) spinnerList.intelligence.clear();
                if(spinnerList.wealth != null) spinnerList.wealth.clear();
                if(spinnerList.height != null) spinnerList.height.clear();
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

    public void writeToFile(String fileName){
        // Saves users spinner selections in strings for readability in writing to file
        String bodyTypeSelection = bodyTypeSpinner.getItemAtPosition(saveBodyPos).toString();
        String intelligenceSelection = intelligenceSpinner.getItemAtPosition(saveIntelligencePos).toString();
        String wealthSelection = wealthSpinner.getItemAtPosition(saveWealthPos).toString();
        String heightSelection = heightSpinner.getItemAtPosition(saveHeightPos).toString();
        File file = new File(fileName);
        try {
            if(!file.exists()) {
                //fos = new FileOutputStream(fosTest, true);
                fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND );
                // If file does not exist, means saved has been cleared. So saved selections should clear too.
                // Maybe a better way to do this.. Fragment 2?
                if(spinnerList.bodyType != null) spinnerList.bodyType.clear();
                if(spinnerList.intelligence != null) spinnerList.intelligence.clear();
                if(spinnerList.wealth != null) spinnerList.wealth.clear();
                if(spinnerList.height != null) spinnerList.height.clear();
//                if(spinnerList != null) {
//                    spinnerList.bodyType.clear();
//                    spinnerList.intelligence.clear();
//                    spinnerList.wealth.clear();
//                    spinnerList.height.clear();
//                }
            }
            Log.d(TAG, "writeToFile: " + bodyTypeSpinner.getItemAtPosition(saveBodyPos).toString());
            Log.d(TAG, "writeToFile: " + intelligenceSpinner.getItemAtPosition(saveIntelligencePos).toString());
            Log.d(TAG, "writeToFile: "+ wealthSpinner.getItemAtPosition(saveWealthPos).toString());
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

}