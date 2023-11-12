package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.LoginActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.UserState
import com.google.firebase.firestore.FirebaseFirestore
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import org.json.JSONObject
import java.math.BigDecimal


class DonateFragment  : Fragment()  {

    override fun onCreateView(lf: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        return lf.inflate(R.layout.fragment_donate, viewGroup, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        val spinner : Spinner = view.findViewById<View>(R.id.spinner) as Spinner
        val amount : TextView = view.findViewById<View>(R.id.amount) as TextView
        val description : TextView = view.findViewById<View>(R.id.description) as TextView
        val button : Button = view.findViewById<View>(R.id.donate_button) as Button

        spinner.adapter = getAdaptor(resources.getStringArray(R.array.donation_type), view.context)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                if (id == 0L) {
                    description.inputType = InputType.TYPE_NULL
                    description.visibility = View.INVISIBLE
                    amount.visibility = View.VISIBLE
                    amount.inputType = InputType.TYPE_CLASS_NUMBER
                } else {
                    amount.inputType = InputType.TYPE_NULL
                    amount.visibility = View.INVISIBLE
                    description.visibility = View.VISIBLE
                    description.inputType = InputType.TYPE_CLASS_TEXT
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        button.setOnClickListener {
            val item = spinner.selectedItem.toString()
            if (item == "Money") {
                val value = amount.text.toString()
                if (value.isEmpty()) {
                    amount.error = "Fill in amount"
                } else {
                    donate(item, value, "Money donated R$value", view.context)
                }
            } else {
                val value = description.text.toString()
                if (value.isEmpty()) {
                    description.error = "Fill in description"
                } else {
                    donate(item, "0", value, view.context)
                }
            }
        }
    }

    private fun donate(type: String, amount: String, description : String, context: Context) {
        if (type == "Money")
            paypalPayment(amount, description)
        else {
            saveData(type, amount, description, context)
        }
    }

    private fun saveData(type: String, amount: String, description: String, context: Context) {
        val user = UserState.getInstance()?.getUser()
        val donations = hashMapOf(
            "id" to user?.getToken(),
            "type" to type,
            "amount" to amount,
            "description" to description
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("donations").add(donations)
            .addOnSuccessListener {
                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getAdaptor(array: Array<String>, context : Context): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            R.layout.spinner_item, array
        )
    }

    private fun paypalPayment(amount : String, description: String) {
        val clientId = "AUr9IFZAmXneWqlUFhc17y6srDGH5A0445W2JvZyYCzIseXm0Qtnq8fOwAQhsoU6vUP_XEPe8hEweDuT"
        val config = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientId)

        val payment = PayPalPayment(BigDecimal(amount), "USD", description, PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val paymentConfirmation : PaymentConfirmation = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)!!
            if (paymentConfirmation != null) {
                val details = paymentConfirmation.toJSONObject().toString()
                val jObject = JSONObject(details)
                saveData("Money", "", "Paypal donation", requireContext())
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Some errors", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(context, "Invalid payment", Toast.LENGTH_SHORT).show()
        }
    }
}