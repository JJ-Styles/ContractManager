/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contractmanager;
import java.io.Serializable;

/**
 *
 * @author t7047098
 */
public class Contract implements Serializable
{
    private String clientName, referenceNumber, contractDate;
    private char internationalCalls;
    private int dataBundle, clientsPackage, periodOfContract, monthlyCharge;
    
    // User defined constuctor for use of he system when bringing in data.
   Contract(String clientNameValue, String referenceNumberValue, String contractDateValue, char intCallsValue, int dataBundleValue, int clientsPackageValue, int periodOfContractValue, int monthlyChargeValue)
   {
       clientName = clientNameValue;
       referenceNumber = referenceNumberValue;
       contractDate = contractDateValue;
       internationalCalls = intCallsValue;
       dataBundle = dataBundleValue;
       clientsPackage = clientsPackageValue;
       periodOfContract = periodOfContractValue;
       monthlyCharge = monthlyChargeValue;
   }
   
   // Default constructor
   Contract()
   {
       clientName = null;
       referenceNumber = null;
       contractDate = null;
       internationalCalls = 'n';
       dataBundle = 0;
       clientsPackage = 0;
       periodOfContract = 0;
       monthlyCharge = 0;
   }

    public String getClientName()
    {
        return clientName;
    }

    public String getReferenceNumber()
    {
        return referenceNumber;
    }

    public String getContractDate()
    {
        return contractDate;
    }

    public char getInternationalCalls()
    {
        return internationalCalls;
    }

    public int getDataBundle()
    {
        return dataBundle;
    }

    public int getClientsPackage()
    {
        return clientsPackage;
    }

    public int getPeriodOfContract()
    {
        return periodOfContract;
    }

    public int getMonthlyCharge()
    {
        return monthlyCharge;
    }

    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }

    public void setReferenceNumber(String referenceNumber)
    {
        this.referenceNumber = referenceNumber;
    }

    public void setContractDate(String contractDate)
    {
        this.contractDate = contractDate;
    }

    public void setInternationalCalls(char internationalCalls)
    {
        this.internationalCalls = internationalCalls;
    }

    public void setDataBundle(int dataBundle)
    {
        this.dataBundle = dataBundle;
    }

    public void setClientsPackage(int clientsPackage)
    {
        this.clientsPackage = clientsPackage;
    }

    public void setPeriodOfContract(int periodOfContract)
    {
        this.periodOfContract = periodOfContract;
    }

    public void setMonthlyCharge(int monthlyCharge)
    {
        this.monthlyCharge = monthlyCharge;
    }
   
}