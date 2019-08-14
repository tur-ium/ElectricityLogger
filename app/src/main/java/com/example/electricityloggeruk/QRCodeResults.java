package com.example.electricityloggeruk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.electricityloggeruk.MainActivity.QRCodeText;

public class QRCodeResults extends AppCompatActivity {
    /**
     * DATA ON CARBON INTENSITIES
     */
    //Annual Average Carbon Intensities in g CO2e / kWh
    public static Map<String, Double> countries_carbon_intensities = new HashMap<String, Double>();

    //UK Energy Supplier and schemes with net zero Carbon. Data from https://www.cable.co.uk/energy/guides/green-energy/
    //TODO: Check details
    //TODO: Enable community feedback
    public static List<String> UK_zero_carbon_suppliers;

    private TextView mElectricityUsage;
    private TextView mStatus;
    private TextView mSupplier;
    private TextView mCarbonFootprint;
    private TextView mCarbonIntensityComment;
    private TextView mCarbonIntensity;

    public static class QRCodeInfo{
        private int _status; //Status: 0 for
        private String _supplier;
        private double _annualElecUsage;
        private double _annualGasUsage;

        /**
         *
         * @param status: 0 if all good, 1 if supplier not recognised, 2 if QR code is null or empty
         * @param supplier: Electricity / Gas supplier
         * @param annualElecUsage: Annual electrity usage in kWh
         * @param annualGasUsage: Annual gas usage in kWh(TODO:)
         */
        public QRCodeInfo(int status, String supplier, double annualElecUsage, double annualGasUsage){
            _status=status;
            _supplier=supplier;
            _annualElecUsage=annualElecUsage;
            _annualGasUsage=annualGasUsage;
        }
        public QRCodeInfo(int status){
            _status = status;
        }
    }
    public QRCodeInfo getElectricityUsage(String QRcode) {
        if (QRcode != null && QRcode != "") {
            String[] params = QRcode.split(",");
            Log.i("MainActivity", "QR code says:" + QRcode);

            //Check if SSE
            if (QRcode.contains("SSE Energy Supply")) {
                Log.i("MainActivity", "SSE");
                return new QRCodeInfo(0, "SSE", Double.parseDouble(params[10]), 0.);
            } else if (QRcode.contains("Ovo Energy")) {
                Log.i("MainActivity", "OVO energy");
                return new QRCodeInfo(0, "OVO Energy", Double.parseDouble(params[10]), 0.);
            } else if (QRcode.contains("E.ON")) {
                Log.i("MainActivity", "E.ON energy");
                return new QRCodeInfo(0, "E.ON", Double.parseDouble(params[10]), 0.);
            } else {
                if (params.length > 10) {
                    Log.i("MainActivity", "Supplier is not recognised");
                    return new QRCodeInfo(0, params[3], Double.parseDouble(params[10]), 0.);
                } else {
                    return new QRCodeInfo(1);
                }
            }
        } else {
            Log.i("MainActivity", "QR code is null or an empty string");
            return new QRCodeInfo(2);
        }
    }
    public void initializeCountryCarbonIntensities(){
        countries_carbon_intensities.put("UK",175.); //In 2018. According to UK government report. https://web.archive.org/web/20190725005545/https://assets.publishing.service.gov.uk/government/uploads/system/uploads/attachment_data/file/794590/updated-energy-and-emissions-projections-2018.pdf
        countries_carbon_intensities.put("France",53.); //IN 2017. According to https://web.archive.org/web/20190814014301/https://www.carbonfootprint.com/docs/2018_8_electricity_factors_august_2018_-_online_sources.pdf
        countries_carbon_intensities.put("Denmark",191.); //In 2017. According to https://web.archive.org/web/20190814014301/https://www.carbonfootprint.com/docs/2018_8_electricity_factors_august_2018_-_online_sources.pdf
        UK_zero_carbon_suppliers = Arrays.asList("Bulb","Tonik","Green Energy UK","Ecotricity","Good Energy", "Solarplicity","So Energy","Pure Planet","Npower Go Green","Co-Op Green Pioneer","Ovo Energy Green Energy add-on");
    }
    public double getCarbonIntensity (String country, String supplier){
        mCarbonIntensity.setVisibility(View.VISIBLE);
        mCarbonIntensityComment.setVisibility(View.VISIBLE);
        if (UK_zero_carbon_suppliers.contains(supplier)){
            mCarbonIntensity.setText("0 g CO2e / kWh");
            mCarbonIntensityComment.setText("We believe your energy supplier is carbon zero");
            return 0;
        }else if(countries_carbon_intensities.containsKey(country)){
            mCarbonIntensity.setText(String.format("%.2f g CO2e / kWh",countries_carbon_intensities.get(country)));
            mCarbonIntensityComment.setText("Using average carbon intensity for country");
            return countries_carbon_intensities.get(country);
        }else{
            mCarbonIntensity.setText("?? g CO2e / kWh");
            mCarbonIntensityComment.setText("Could not find caron intensity for your supplier/country, why not join the effort to make it easier for people to do carbon footprinting in your country? Say hi on our Slack channel!");
            return -1;
        }
    }

    /**
     *
     * @param country
     * @param supplier
     * @param annualElecUsage
     * @return Annual Electricity Carbon Footprint in kg CO2e
     */
    public double getCarbonFootprint(String country,String supplier,double annualElecUsage){
        double carbonIntensity = getCarbonIntensity(country,supplier);
        return annualElecUsage*carbonIntensity*.001;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        mElectricityUsage = findViewById(R.id.electricity);
        mStatus = findViewById(R.id.status);
        mCarbonFootprint = findViewById(R.id.carbonFootprint);
        mCarbonIntensityComment = findViewById(R.id.carbonIntensityComment);
        mCarbonIntensity = findViewById(R.id.CarbonIntensity);
        mSupplier = findViewById(R.id.supplierName);

        initializeCountryCarbonIntensities();

        if (QRCodeText != null && QRCodeText != "") {

            QRCodeInfo info = getElectricityUsage(QRCodeText);
            if (info._status == 0) {
                mStatus.setText("SUCCESSFULLY SCANNED QR CODE");
                mElectricityUsage.setVisibility(View.VISIBLE);
                mElectricityUsage.setText("Estimated annual electricity usage: " + info._annualElecUsage + " kWh / year");
                mSupplier.setText("Supplier: " + info._supplier);
                mSupplier.setVisibility(View.VISIBLE);

                double carbonFootprint = getCarbonFootprint("UK",info._supplier,info._annualElecUsage);
                mCarbonFootprint.setText(String.format("Annual electricity carbon footprint : %.1f kg CO2e",carbonFootprint));
                mCarbonFootprint.setVisibility(View.VISIBLE);

                mSupplier.setVisibility(View.VISIBLE);
            } else if (info._status == 2) {
                mStatus.setText("QR code is in the wrong format or you didn't scan a QR code. Please check you are scanning the right code and press the back button to scan again. You can find more information about QR codes here: https://www.energycompanynumbers.co.uk/what-are-qr-codes-energy-bill/");
            } else {
                mStatus.setText("Couldn't understand QR code, please check you are scanning the right thing and try again. Press the back button to scan again. If this error occurs again please email arturdonaldson@protonmail.com with your energy supplier");
            }

        }

    }
}
