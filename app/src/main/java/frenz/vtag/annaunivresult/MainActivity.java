package frenz.vtag.annaunivresult;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private TextInputLayout textInputLayout;
    private EditText etRegisterNo;

    private AppCompatButton btnSubmit;
    private LinearLayout resultContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        textInputLayout = (TextInputLayout) findViewById(R.id.tip);
        etRegisterNo = (EditText) findViewById(R.id.etRegNo);
        btnSubmit = (AppCompatButton) findViewById(R.id.btnSubmit);
        resultContainer = (LinearLayout) findViewById(R.id.resultContainer);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResult();
            }
        });
    }


    private void getResult() {
        etRegisterNo.setError(null);

        final String regNo = etRegisterNo.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(regNo)) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Register number cannot be empty");
            focusView = etRegisterNo;
            cancel = true;
        } else if (!isRegNoValid(regNo)) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("12 digit register number required");
            focusView = etRegisterNo;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            ApplicationController.getInstance().getRequestQueue().add(new StringRequest(StringRequest.URL_ANNA_UNIV + regNo, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    handleResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(context, "Make sure you have a working network connection. Please enable WIFI or MOBILE DATA.", Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError){
                        Toast.makeText(context, "Server is taking too long to respond. Please try later", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }));
        }
    }

    void handleResponse(String response){
        Document doc = Jsoup.parse(response);

        ArrayList<ModelResult> resultArrayList = new ArrayList<>();
        for (Element row : doc.select("tr")) {
            Elements tds = row.select("td");
            if (tds.size()==1) {
                resultArrayList.add(new ModelResult(null, tds.get(0).text(), null, true));
            } else if (tds.size()==3) {
                resultArrayList.add(new ModelResult(tds.get(0).text(), tds.get(1).text(), tds.get(2).text(), !tds.get(0).text().matches("^[A-Za-z]{2}[0-9]{4}$")));
            }
        }

        populateResult(resultArrayList);

    }

    void populateResult(ArrayList<ModelResult> resultArrayList){
        resultContainer.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        for (ModelResult result : resultArrayList) {
            if (result.getItemOne()==null) {
                View view = layoutInflater.inflate(R.layout.list_result_header, null);
                ((TextView) view.findViewById(R.id.itemOne)).setText(result.getItemTwo());
                resultContainer.addView(view);
            } else if (result.getItemOne().matches("^[0-9]+$")) {
                View view = layoutInflater.inflate(R.layout.list_result_student, null);
                ((TextView) view.findViewById(R.id.itemOne)).setText(result.getItemOne());
                ((TextView) view.findViewById(R.id.itemTwo)).setText(result.getItemTwo());
                ((TextView) view.findViewById(R.id.itemThree)).setText(result.getItemThree());
                resultContainer.addView(view);
            } else if (result.isResultHeader()) {
                View view = layoutInflater.inflate(R.layout.list_result_item, null);
                ((TextView) view.findViewById(R.id.itemOne)).setText(result.getItemOne());
                ((TextView) view.findViewById(R.id.itemTwo)).setText(result.getItemTwo());
                ((TextView) view.findViewById(R.id.itemThree)).setText(result.getItemThree());
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                resultContainer.addView(view);
            } else {
                View view = layoutInflater.inflate(R.layout.list_result_item, null);
                ((TextView) view.findViewById(R.id.itemOne)).setText(result.getItemOne());
                ((TextView) view.findViewById(R.id.itemTwo)).setText(result.getItemTwo());
                ((TextView) view.findViewById(R.id.itemThree)).setText(result.getItemThree());
                view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                resultContainer.addView(view);
            }


        }
    }

    boolean isRegNoValid(String registerNo){
        return registerNo.length()>8;
    }


}
