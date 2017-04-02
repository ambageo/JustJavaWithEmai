

    /**
     * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
     * This is the package name our example uses:
     *
     * package com.example.android.justjava;
     */
    package com.example.android.justjava;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.util.Log;
    import android.view.View;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import static android.R.attr.order;
    import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

    /**
     * This app displays an order form to order coffee.
     */
    public class MainActivity extends AppCompatActivity {

        int quantity=0;
        int basePrice=5;
        int whippedCreamPrice=1;
        int chocolatePrice=2;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }

        /**
         * This method is called when the order button is clicked.
         */
        public void submitOrder(View view) {

            CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
            boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
            CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
            boolean hasChocolate = chocolateCheckBox.isChecked();
            EditText customerNameText= (EditText) findViewById(R.id.customer_name);
            String customerName= customerNameText.getText().toString();
            int price=calculatePrice(hasWhippedCream,hasChocolate);
            String orderSummary=createOrderSummary(price, hasWhippedCream, hasChocolate, customerName);

//          The following code creates an intent to send the order to an email
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT,"JustJava order for " + customerName);
            intent.putExtra(Intent.EXTRA_TEXT,orderSummary);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }

        /**
         * Calculates the price of the order, according to what the user chooses
         * @return total price of the order
         */
        private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {

            if(hasWhippedCream){ //if the customer chose whipped cream topping
                if(hasChocolate){ //and if the customer chose chocolate
                    return quantity*(basePrice + whippedCreamPrice + chocolatePrice);
                }
                else //the customer chose only whipped cream
                    return quantity*(basePrice+ whippedCreamPrice);
            }
            else if(hasChocolate){ //the customer chose only chocolate
                return quantity*(basePrice + chocolatePrice);
            }
            else //the customer didn't choose any topping
                return quantity*5;
        }

        /**
         * @param price total price of the order
         * @param addedWhippedCream whether or not customer ordered whipped cream
         * @param addedChocolate  whether or not customer ordered chocolate
         * @param customerName the name that the customers inputs
         * @return summary of the order
         */
        private String createOrderSummary(int price, boolean addedWhippedCream, boolean addedChocolate, String customerName){
            String orderSummary="Name: " + customerName + "\n";
            orderSummary=orderSummary + "Add Whipped Cream? " + addedWhippedCream + "\n";
            orderSummary=orderSummary + "Add Chocolate?" + addedChocolate + "\n";
            orderSummary=orderSummary + "Quantity: " + quantity + "\n";
            orderSummary= orderSummary + "Total: €" + price + "\n" + "Thank you!";
            return orderSummary;
        }

        /**
         * This method displays the given quantity value on the screen.
         */
        private void display(int number) {
            TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
            quantityTextView.setText("" + number);
        }

        /**
         * This method is called when the "+" button is clicked
         */
        public void increment(View view){
            quantity=quantity+1;
            //if quantity more than 100, pop-up an error message
            if(quantity>100) {
                Toast.makeText(this, "You cannot order more than 100 cups", Toast.LENGTH_SHORT).show();
                //kepp the quantity at the maximum desired
                quantity=100;
                //exit method by mot incrementing the quantity
                return;
            }
            display(quantity);
        }

        /**
         * This method is called when the "-" button is clicked
         */
        public void decrement(View view){
            quantity=quantity-1;
            //if quantity less than 1, pop-up an error message
            if(quantity<1){
                Toast.makeText(this, "You cannot order less than 1 cup", Toast.LENGTH_SHORT).show();
                //keep the quantity at the minimum desired
                quantity=1;
                //exit method by not decrementing the quantity
                return;
            }
            display(quantity);
        }

        /**
         * This method displays the given text on the screen.
         */
        private void displayMessage(String message) {
            TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
            orderSummaryTextView.setText(message);
        }
    }