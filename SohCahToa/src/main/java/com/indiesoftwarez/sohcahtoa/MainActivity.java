package com.indiesoftwarez.sohcahtoa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private Context context = this;

    private static final String TAG = "SohCahToa";

    private double oppositeValue, adjacentValue, hypotenuseValue, angleValue;

    private boolean rightAngle = true;
    private boolean degrees = true;
    private boolean radians = false;

    private Button clearButton, quitButton, infoButton, computeButton;

    private Editable oField, aField, hField, angleField;
    private EditText oEditText, aEditText, hEditText, angleEditText;

    private int selectionCount;

    private RadioGroup radioGroupdId;
    private RadioButton degreesRadioButton;
    private RadioButton radiansRadioButton;
    private int selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the widget for EditText to variable.
        oEditText = (EditText)findViewById(R.id.oValue);
        oField = oEditText.getText();

        aEditText = (EditText)findViewById(R.id.aValue);
        aField = aEditText.getText();

        hEditText = (EditText)findViewById(R.id.hValue);
        hField = hEditText.getText();

        angleEditText = (EditText)findViewById(R.id.angleValue);
        angleField = angleEditText.getText();

        // Create and add listener for clear button.
        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearValues();
            }
        });

        // Create and add listener for quit button.
        quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExit();
            }
        });

        // Create and add listener for info button.
        infoButton = (Button) findViewById(R.id.InfoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();
            }
        });

        radioGroupdId = (RadioGroup) findViewById(R.id.radioDegRadGroup);
        radioGroupdId.setOnCheckedChangeListener(this);

        computeButton = (Button) findViewById(R.id.computeButton);
        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get values and error check.
                getValues();

                // Do final calculation.
                doCompute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
        return false;
    }


    /**
     * Get the values and determine if there is an input error.
     */
    private void getValues()
    {
        selectionCount = 0;

        if (!TextUtils.isEmpty(oEditText.getText().toString()))
        {
            oppositeValue = Double.parseDouble(oEditText.getText().toString());
            selectionCount++;
        }

        if (!TextUtils.isEmpty(aEditText.getText().toString()))
        {
            adjacentValue = Double.parseDouble(aEditText.getText().toString());
            selectionCount++;
        }

        if (!TextUtils.isEmpty(hEditText.getText().toString()))
        {
            hypotenuseValue = Double.parseDouble(hEditText.getText().toString());
            selectionCount++;
        }

        if (!TextUtils.isEmpty(angleEditText.getText().toString()))
        {
            angleValue = Double.parseDouble(angleEditText.getText().toString());
            selectionCount++;
        }

    }


    /**
     * Compute all the side lengths and angle.  Change the display to two decimal places.
     *
     * O and A are known.  So o^2 + a^ = h^
     */
    private void doCompute()
    {
        // get the selected radio button from the group
        selectedOption = radioGroupdId.getCheckedRadioButtonId();

        Log.d(TAG, "radioDegRadButton: " + selectedOption);

        // Rule check.
        if (checkRules())
        {
            return;
        }


        // O and A are know. H? Angle ?
        if (oppositeValue != 0 && adjacentValue != 0)
        {
            // Find the hypotenuse.
            hypotenuseValue = Math.sqrt((oppositeValue * oppositeValue) + (adjacentValue * adjacentValue));

            // Now we have O, A and H, find the angle.
            angleValue = Math.acos(adjacentValue/hypotenuseValue)*180/Math.PI;

            // Convert to 2 decimal place.
            angleValue = Math.round(angleValue * 100.0) / 100.0;
            // Change the display.
            angleEditText.setText(Double.toString(angleValue));

            // Convert to 2 decimal places.
            hypotenuseValue = Math.round(hypotenuseValue * 100.0) / 100.0;
            // Change the display.
            hEditText.setText(Double.toString(hypotenuseValue));

            return;
        }

        // We know O and H. So, A? angle?
        if (oppositeValue !=0 && hypotenuseValue != 0)
        {
            // Get the angle.
            angleValue = Math.asin((oppositeValue / hypotenuseValue));

            // Now we have the angle, what is the adjacent?
            adjacentValue = oppositeValue / Math.tan(angleValue);

            // Convert the angle to degrees.
            angleValue = Math.toDegrees(angleValue);
            // Convert to 2 decimal place.
            angleValue = Math.round(angleValue * 100.0) / 100.0;
            // Change the display.
            angleEditText.setText(Double.toString(angleValue));

            // Convert to 2 decimal places.
            adjacentValue = Math.round(adjacentValue * 100.0) / 100.0;
            // Change the display.
            aEditText.setText(Double.toString(adjacentValue));

            return;
        }

        // We know the O and angle.  So, A? and H?
        if (oppositeValue != 0 && angleValue != 0)
        {
            // What is the adjacent?
            adjacentValue = oppositeValue / Math.tan(Math.toRadians(angleValue));

            // What is the hypotenuse?
            hypotenuseValue = adjacentValue / Math.cos(Math.toRadians(angleValue));

            // Convert to 2 decimal places.
            hypotenuseValue = Math.round(hypotenuseValue * 100.0) / 100.0;
            // Change the display.
            hEditText.setText(Double.toString(hypotenuseValue));

            // Convert to 2 decimal places.
            adjacentValue = Math.round(adjacentValue * 100.0) / 100.0;
            // Change the display.
            aEditText.setText(Double.toString(adjacentValue));

            return;
        }

        // We know the A and H. So, O? and angle?
        if (adjacentValue != 0 && hypotenuseValue !=0)
        {
            angleValue = Math.acos(adjacentValue / hypotenuseValue);

            // Convert the angle to degrees.
            angleValue = Math.toDegrees(angleValue);
            // Convert to 2 decimal place.
            angleValue = Math.round(angleValue * 100.0) / 100.0;
            // Change the display.
            angleEditText.setText(Double.toString(angleValue));

            // Now we have the angle what is the opposite?
            oppositeValue = hypotenuseValue * Math.sin(Math.toRadians(angleValue));

            // Convert to 1 decimal place.
            oppositeValue = Math.round(oppositeValue * 100.0) / 100.0;

            // Change the display.
            oEditText.setText(Double.toString(oppositeValue));

            return;
        }

        // We know the A and angle. So, O? and h?
        if (adjacentValue != 0 && angleValue != 0)
        {
            // What is the hypotenuse?
            hypotenuseValue = adjacentValue / Math.cos(Math.toRadians(angleValue));

            // Convert to 2 decimal places.
            hypotenuseValue = Math.round(hypotenuseValue * 100.0) / 100.0;
            // Change the display.
            hEditText.setText(Double.toString(hypotenuseValue));

            // Now we have the angle what is the opposite?
            oppositeValue = hypotenuseValue * Math.sin(Math.toRadians(angleValue));
            // Convert to 1 decimal place.
            oppositeValue = Math.round(oppositeValue * 100.0) / 100.0;
            // Change the display.
            oEditText.setText(Double.toString(oppositeValue));

            return;
        }

        // We know the angle and the h.  So, a? and o?
        if (angleValue != 0 && hypotenuseValue != 0)
        {
            // Perform soh and store the new opposite
            oppositeValue = hypotenuseValue * Math.sin(Math.toRadians(angleValue));

            // Convert to 1 decimal place.
            oppositeValue = Math.round(oppositeValue * 100.0) / 100.0;
            // Change the display.
            oEditText.setText(Double.toString(oppositeValue));

            // Perform soh and store the new opposite
            adjacentValue = oppositeValue / Math.tan(Math.toRadians(angleValue));

            // Convert to 1 decimal place.
            adjacentValue = Math.round(adjacentValue * 100.0) / 100.0;
            // Change the display.
            aEditText.setText(Double.toString(adjacentValue));
        }
    }


    /**
     * Check values entered against rules.
     *
     * @return true if the rules are not adhered to, else return false for no error.
     */
    private boolean checkRules()
    {
        // Check for more than 2 values entered.
        if (selectionCount > 2)
        {
            showError();
            return true;
        }

        // Are we checking SOH?
        if (oppositeValue != 0 && hypotenuseValue != 0)
        {
            // Rule 1 and 3. o >= h
            if (oppositeValue >= hypotenuseValue)
            {
                showError();
                return true;
            }
        }

        // Are we checking CAH?
        if (adjacentValue != 0 && hypotenuseValue != 0)
        {
            // Rule 2 and 4. a >= h
            if (adjacentValue == hypotenuseValue)
            {
                showError();
                return true;
            }
        }

        // Is right-angle triangle set.
        if (rightAngle && hypotenuseValue != 0)
        {
            // Rule 3 and 4.
            if (oppositeValue > hypotenuseValue || adjacentValue > hypotenuseValue)
            {
                showError();
                return true;
            }
        }

        // Rule 5. angle < 90
        if (angleValue >= 90)
        {
            showError();
            return true;
        }

        // Return false to signal no errors found.
        return false;
    }


    /**
     * Show error dialog box and clear the values.
     */
    private void showError()
    {
        // Clear the values.
        clearValues();

        // Create an AlertDialog.Builder()
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("Error!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Enter ONLY two values and select compute.  With the angle being less than 90 degrees, " +
                        "the opposite or adjacent can't be the same as the hypotenuse.  " +
                        "In right-angled triangles the opposite or adjacent can't be bigger than the hypotenuse.")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    /**
     * Show info dialog box.
     */
    private void showInfo()
    {
        // Create an AlertDialog.Builder()
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("SohCahToa");

        // set dialog message
        alertDialogBuilder
                .setMessage("Enter two values and select compute.  Since this is an early version, only degrees is working at the moment.  " +
                        "With the release of the next version I plan to have the option for radians working and a choice of variable angles.\n\n" +
                        "I for information.\nC to clear.\nQ to quit.\n\n" +
                        "Darren Tynan\nIndieSoftwarez.com")
                .setCancelable(true)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    /**
     * Show exit dialog box.
     */
    private void showExit()
    {
        // Create an AlertDialog.Builder()
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("SohCahToa");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to quit?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    /**
     * Clear the EditText widget values to initial hint status.
     */
    private void clearValues()
    {
        oEditText.setText("");
        aEditText.setText("");
        hEditText.setText("");
        angleEditText.setText("");

        oppositeValue = 0;
        adjacentValue = 0;
        hypotenuseValue = 0;
        angleValue = 0;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.degreesRadioButton)
        {
            Log.d(TAG, "DEGREES");
        }
        if (checkedId == R.id.radiansRadioButton)
        {
            Log.d(TAG, "RADIANS");
        }
    }
}
