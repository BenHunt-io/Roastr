package layout;
import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exetxstate.bullytoolkit.MainActivity;
import com.exetxstate.bullytoolkit.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Ben on 1/11/2017.
 */
class SingleRow // stores the three arrays in the form of complex object. Used for getCount method

{
    String title;
    String description;
    int image;
    int xButtonImage;

    SingleRow(String title, String description, int image, int xButtonImage){  // This refers to current instance of class
        this.title = title;
        this.description = description;
        this.image = image;
        this.xButtonImage = xButtonImage;
    }

}
public class MyCustomAdapter extends BaseAdapter{
    String sortFileName = "sortedFile";
    String savedSelectionsFile = "selectionsFile";
    ArrayList<SingleRow> list;
    int image = R.drawable.lowerresroastr;
    int xButtonImage = R.drawable.xclearselector;
    Context context;
    public SpinnerSelections selectionsList;
    public ArrayList<String> saveNewList;

    MyCustomAdapter(Context c, ArrayList<String> newList, SpinnerSelections spinnerSelections)
    {
        selectionsList = spinnerSelections;
        saveNewList = newList;
        // transfer the values from fragment 2 to custom adapter through the constructor.
        // I could just make the makeDescription fucntion in fragment 2.

        Log.d(TAG, "MyCustomAdapter: " + newList);
        context = c;
        list = new ArrayList<SingleRow>();
        for(int i = 0; i < newList.size(); i++){
            //Log.d(TAG,selectionsList.bodyType.get(i));
            list.add(i, new SingleRow(newList.get(i), makeDescription(i) , image, xButtonImage)); // put makeDescription(i) back in.. After testing
        }
        Log.d(TAG, "MyCustomAdapter:" + list);
    }

    @Override
    public int getCount() { // Return number of elements inside this array
        return list.size();
    }

    @Override
    public Object getItem(int position) { // return the object at position arg0
        return list.get(position);
    }

    @Override
    public long getItemId(int i) { // return the id of the row which is our array index itself in this case
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {  // returns a view that was converted. listview will call base adapters
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   // getView method  // We have our RelativeLayout
        View row = inflater.inflate(R.layout.single_row, parent, false);
        TextView title = (TextView) row.findViewById(R.id.sentenceTitle);
        TextView description = (TextView) row.findViewById(R.id.description); // Found those views over here
        ImageView image = (ImageView) row.findViewById(R.id.imageView);
        ImageView xButton = (ImageView) row.findViewById(R.id.xButton);
        SingleRow temp = list.get(i);

        title.setText(temp.title);
        description.setText(temp.description);     // Set the views values
        image.setImageResource(temp.image);
        xButton.setImageResource(temp.xButtonImage);

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2.deleteRow(i, context);

            }
        });

        return row;


    }

    public String makeDescription(int position){
        fragment1 fragment1Obj = new fragment1();
        String description;
        final int numOfSelections = 4; // constant for now.. but maybe change in future with development of app
        String[] selections = new String[numOfSelections];
        selections[0] = selectionsList.bodyType.get(position);  // position here corresponds with i when it is used in constructor
        selections[1] = selectionsList.intelligence.get(position);
        selections[2] = selectionsList.wealth.get(position);
        selections[3] = selectionsList.height.get(position);

        description = "Selections: " + selections[0] + " | " + selections[1] + " | " + selections[2] +
                " | " + selections[3];


        return description;
    }
}
