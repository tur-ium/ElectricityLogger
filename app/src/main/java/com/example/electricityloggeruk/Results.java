package com.example.electricityloggeruk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results extends AppCompatActivity {
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
    private TextView getCarbonIntensity;
    private TextView mCarbonIntensityComment;
    private TextView mCarbonIntensity;

    public static double elecUsage;
    public static String supplier;
    public static String country;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        mElectricityUsage = findViewById(R.id.electricity);
        mStatus = findViewById(R.id.status);
        mCarbonFootprint = findViewById(R.id.carbonFootprint);
        mSupplier = findViewById(R.id.supplierName);
        mCarbonIntensityComment = findViewById(R.id.carbonIntensityComment);
        mCarbonIntensity = findViewById(R.id.CarbonIntensity);

        initializeCountryCarbonIntensities();

        mElectricityUsage.setVisibility(View.VISIBLE);
        mElectricityUsage.setText("Estimated annual electricity usage: " + elecUsage + " kWh / year");

        mSupplier.setText("Supplier: " + supplier);
        mSupplier.setVisibility(View.VISIBLE);

        double carbonFootprint = getCarbonFootprint("UK",supplier,elecUsage);
        mCarbonFootprint.setText(String.format("Annual electricity carbon footprint : %.1f kg CO2e",carbonFootprint));
        mCarbonFootprint.setVisibility(View.VISIBLE);

        mSupplier.setVisibility(View.VISIBLE);
    }


    public void initializeCountryCarbonIntensities() {
        countries_carbon_intensities.put("UK", 175.); //In 2018. According to UK government report. https://web.archive.org/web/20190725005545/https://assets.publishing.service.gov.uk/government/uploads/system/uploads/attachment_data/file/794590/updated-energy-and-emissions-projections-2018.pdf
        countries_carbon_intensities.put("France", 53.); //IN 2017. According to https://web.archive.org/web/20190814014301/https://www.carbonfootprint.com/docs/2018_8_electricity_factors_august_2018_-_online_sources.pdf
        countries_carbon_intensities.put("Denmark", 191.); //In 2017. According to https://web.archive.org/web/20190814014301/https://www.carbonfootprint.com/docs/2018_8_electricity_factors_august_2018_-_online_sources.pdf
        UK_zero_carbon_suppliers = Arrays.asList("Bulb", "Tonik", "Green Energy UK", "Ecotricity", "Good Energy", "Solarplicity", "So Energy", "Pure Planet", "Npower Go Green", "Co-Op Green Pioneer", "Ovo Energy Green Energy add-on");
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
}