/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contractmanager;

import java.io.File;
import java.util.Scanner;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.io.IOException;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A prototype application for a contract manager system
 *
 * @author t7047098
 */
public class ContractManager implements Serializable
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        int menuOption = 10;                    // Sets menuOption outside of the range so the while will take place and it wont catch the case if a error occured.
        Contract newClientContract = new Contract();

        //Catches any input that is not 0 and repeats the menu
        while (menuOption != 0)
        {
            System.out.println("1. Enter new Contract");
            System.out.println("2. Display Summary of Contracts");
            System.out.println("3. Display Sumaary of Contracts from Selected Month");
            System.out.println("4. Find and Display Contract");
            System.out.println("0. Exit");
            try
            {
                menuOption = keyboard.nextInt();

                // Cycles through which menuOption has been choosen and carries out the option required.
                switch (menuOption)
                {
                    case 1:
                        newContract(newClientContract);
                        break;
                    case 2:
                        summaryOfContract(newClientContract);
                        break;
                    case 3:
                        summaryOfMonthlyContract(newClientContract);
                        break;
                    case 4:
                        findDisplayContract(newClientContract);
                        break;
                    case 0:
                        System.out.println("GoodBye. Have a nice day.");
                        break;
                    default:
                        System.out.println("Please enter one of the options");
                        break;
                }
            } catch (Exception e)
            {
                System.out.println(e);
                System.out.println("Please enter a menuOption");
            }
        }
    }

    public static void newContract(Contract newClientContract)
    {
        Scanner keyboard = new Scanner(System.in);
        String clientName = "", referenceNumber = "", contractDate = "", packageBundle;
        char internationalCalls = 'n', businessCustomer = 'n';
        int periodOfContract = 0, monthlyCharge;
        double discount = 0;

        //Asks the user to input a valid Name and sets the name to the object
        while (clientName.length() > 26 || clientName.equals(""))
        {
            System.out.println("Please enter the Client Name");
            clientName = keyboard.nextLine();
            if (clientName.length() > 26 || clientName.length() == 0)
            {
                System.out.println("Please enter a name that is less than 26 characters or that a valid name that is not an empty string");
            } else
            {
                newClientContract.setClientName(clientName);
            }
        }

        getPackageAndDataBundle(newClientContract);             //gets the clients package and data bundle and sets it to the object

        //Sets the value to the object for whether the client wants international calls and handles if an incorrect value is entered
        while (internationalCalls != 'N' && internationalCalls != 'Y')
        {
            try
            {
                System.out.println("Please Enter (Y/N) whether you want international Calls");
                internationalCalls = keyboard.nextLine().toUpperCase().charAt(0);
                if (internationalCalls != 'N' && internationalCalls != 'Y')
                {
                    System.out.println("Please enter Y/N");
                } else
                {
                    newClientContract.setInternationalCalls(internationalCalls);
                }
            } catch (Exception error)
            {
                System.out.println("Please Enter (Y/N)");
            }
        }

        //Sets the value to the object for whether the client is a business customer and handles if an incorrect value is entered
        while (businessCustomer != 'Y' && businessCustomer != 'N')
        {
            try
            {
                System.out.println("Are you a business customer (Y/N)");
                businessCustomer = keyboard.nextLine().toUpperCase().charAt(0);
                if (businessCustomer != 'Y' && businessCustomer != 'N')
                {
                    System.out.println("Please enter Y/N");
                }
            } catch (Exception error)
            {
                System.out.println("Please enter Y/N");
            }
        }

        //Checks if the user is a business customer and handles the contract size of the contract availavle and handles any incorrect values
        if (businessCustomer == 'Y')
        {
            while (periodOfContract != 12 && periodOfContract != 18 && periodOfContract != 24)
            {
                try
                {
                    System.out.println("Please enter the period of the contract you would like? (12/18/24)");
                    periodOfContract = keyboard.nextInt();
                    if (periodOfContract != 12 && periodOfContract != 18 && periodOfContract != 24)
                    {
                        System.out.println("Please enter 12, 18 or 24");
                    } else
                    {
                        newClientContract.setPeriodOfContract(periodOfContract);        //sets the lenght of the contract to the object
                    }
                } catch (Exception error)
                {
                    System.out.println("Please Enter a Numerical value");
                }
            }
        } else
        {
            while (periodOfContract != 1 && periodOfContract != 12 && periodOfContract != 18 && periodOfContract != 24)
            {
                try
                {
                    System.out.println("Please enter the period of the contract you would like? (1/12/18/24)");
                    periodOfContract = keyboard.nextInt();
                    if (periodOfContract != 1 && periodOfContract != 12 && periodOfContract != 18 && periodOfContract != 24)
                    {
                        System.out.println("Please enter 1, 12 ,18 or 24");
                    } else
                    {
                        newClientContract.setPeriodOfContract(periodOfContract);            //Sets the length of the contract to the object
                    }
                } catch (Exception error)
                {
                    System.out.println("Please Enter a Numerical value");
                }
            }
        }

        discount = calculateDiscount(periodOfContract, businessCustomer, internationalCalls);        //Calls the function and returns the amount of discount the client will recieve
        calculateCharge(newClientContract, discount);                      //Calls the function and sets the monthly vost to the object
        getDate(newClientContract);                                                                    //Calls the function which sets todays date to the object for the date the contract was taken out
        generateReferenceNumber(businessCustomer, newClientContract);   //Calls the function which calculates the reference number and sets it to the object.

        //Calls the procedure which will generate the summary of the contract
        generateSummary(newClientContract, businessCustomer, discount);

        saveToFile(newClientContract);      //takes the object and saves it to the file.

    }

    public static void getPackageAndDataBundle(Contract client)
    {
        Scanner keyboard = new Scanner(System.in);
        String packageNBundle;
        char packageMenuOption = 'n';
        int minutes = -1, data = -1, dataBundle = 0, clientsPackage = 0;

        //Checks how the user wants to enter there dataBundle and clientsPackage and handles any errors
        while (packageMenuOption != 'a' && packageMenuOption != 'b')
        {
            System.out.println("a) Enter Data Builder and Package");
            System.out.println("b) Enter Estimated Minutes and Data (megbytes)");
            packageMenuOption = keyboard.nextLine().toLowerCase().charAt(0);
            if (packageMenuOption != 'a' && packageMenuOption != 'b')
            {
                System.out.println("Please enter a or b.");
            }
        }

        //Checks the option from the user and asks the user to enter data and handles any errorenous data.
        if (packageMenuOption == 'a')
        {
            while (dataBundle < 1 || dataBundle > 4)
            {
                try
                {
                    System.out.println("Please Enter the Data Bundle (1 = Low, 2 = Medium, 3 = High, 4 = Unlimited)");
                    dataBundle = keyboard.nextInt();
                    if (dataBundle < 1 || dataBundle > 4)
                    {
                        System.out.println("Please enter 1, 2, 3 or 4.");
                    } else
                    {
                        client.setDataBundle(dataBundle);               //sets the bundle to the object
                    }
                } catch (Exception error)
                {
                    System.out.println("Please Enter a Numerical value");
                }
            }

            //checks what package the user wants and sets it to the object if the option is valid
            while (clientsPackage < 1 || clientsPackage > 3)
            {
                try
                {
                    System.out.println("Please Enter the Package (1 = Small, 2 = Medium, 3 = Large)");
                    clientsPackage = keyboard.nextInt();
                    if (clientsPackage < 1 || clientsPackage > 3)
                    {
                        System.out.println("Please enter 1, 2 or 3.");
                    } else
                    {
                        client.setClientsPackage(clientsPackage);
                    }
                } catch (Exception error)
                {
                    System.out.println("Please Enter a Numerical value");
                }
            }
        } else if (packageMenuOption == 'b')
        {
            //checks that the user has entered a valid value for the number of minutes they would like
            while (minutes < 0 || minutes > 1200)
            {
                try
                {
                    System.out.println("Please Enter the Estimated Minutes");
                    minutes = keyboard.nextInt();
                    if (minutes < 0 || minutes > 1200)
                    {
                        System.out.println("Please enter a value between 1 and 1200. We cannot cater above 1200 minutes.");
                    }
                } catch (Exception error)
                {
                    System.out.println("Please Enter a Numerical value.");
                }
            }

            //checks the user has entered a valid amount of data.
            while (data < 0)
            {
                try
                {
                    System.out.println("Please Enter the Estimated Data in MB");
                    data = keyboard.nextInt();
                    if (data < 0)
                    {
                        System.out.println("Please enter a value greater than 0.");
                    }
                } catch (Exception error)
                {
                    System.out.println("Please Enter a Numerical value");
                }
            }

            //sets the correct package deal for the amount of minutes required to the object
            if (minutes > 600)
            {
                client.setClientsPackage(3);
            } else if (minutes > 300)
            {
                client.setClientsPackage(2);
            } else
            {
                client.setClientsPackage(1);
            }

            //sets the correct amount of data for the a mount of data required to the object
            if (data > 8192)
            {
                client.setDataBundle(4);
            } else if (data > 4096)
            {
                client.setDataBundle(3);
            } else if (data > 1024)
            {
                client.setDataBundle(2);
            } else
            {
                client.setDataBundle(1);
            }
        }

        //Checks that the user or system hasn't entered a dataBundle and clientsPackage that is unavailable
        if (dataBundle == 4)
        {
            if (clientsPackage != 3)
            {
                System.out.println("The Package must be a large package for unlimited data.");
                System.out.println("The large Package has been automatically assigned.");
                client.setClientsPackage(3);
            }
        }
    }

    public static double calculateDiscount(int periodOfContract, char businessCustomer, char internationalCalls)
    {
        double discount = 0;

        //Checks if the user is a business customer to give them a discounts where required
        if (businessCustomer == 'Y')
        {
            discount = 10;
        } else if (periodOfContract == 12 || periodOfContract == 18)
        {
            discount = 5;
        } else if (periodOfContract == 24)
        {
            discount = 10;
        }

        //Checks if the client has asked for international calls and takes off discount to increase the price
        if (internationalCalls == 'Y')
        {
            discount -= 15;
        }

        return discount / 100;        //returns the discount as a decimal ready to use
    }

    public static void calculateCharge(Contract client, double discount)
    {
        int charge;

        //Calculates what charge the user should be charged based on there package and dataBundle
        if (client.getClientsPackage() == 1)
        {
            if (client.getDataBundle() == 1)
            {
                charge = 500;
            } else if (client.getDataBundle() == 2)
            {
                charge = 700;
            } else
            {
                charge = 900;
            }
        } else if (client.getClientsPackage() == 2)
        {
            if (client.getDataBundle() == 1)
            {
                charge = 650;
            } else if (client.getDataBundle() == 2)
            {
                charge = 850;
            } else
            {
                charge = 1050;
            }
        } else if (client.getDataBundle() == 1)
        {
            charge = 850;
        } else if (client.getDataBundle() == 2)
        {
            charge = 1050;
        } else if (client.getDataBundle() == 3)
        {
            charge = 1250;
        } else
        {
            charge = 2000;
        }

        discount = 1 - discount;            //Will convert the discount value into an increase or decrease decimal value for a percentage
        charge *= discount;                 //Calculates the charge once the discount has been added
        client.setMonthlyCharge(charge);    //sets the charge to the object
    }

    public static void generateReferenceNumber(char businessCustomer, Contract client)
    {
        String referenceNumber = "";
        Random n = new Random();
        int randomNumber = n.nextInt(10);
        String[] names = new String[5];

        //Generates a referenceNumber for the user dependant on the name of the user and handles any errors
        try
        {
            names = client.getClientName().split(" ");
            referenceNumber = names[0].substring(0, 1);
            referenceNumber += names[1].substring(0, 1);
            referenceNumber += randomNumber;
            randomNumber = n.nextInt(10);
            referenceNumber += randomNumber;
            randomNumber = n.nextInt(10);
            referenceNumber += randomNumber;

            if (businessCustomer == 'Y')
            {
                referenceNumber += "B";
            } else
            {
                referenceNumber += "N";
            }
        } catch (Exception error)
        {
            referenceNumber += "a";
            referenceNumber += randomNumber;
            randomNumber = n.nextInt(10);
            referenceNumber += randomNumber;
            randomNumber = n.nextInt(10);
            referenceNumber += randomNumber;

            if (businessCustomer == 'Y')
            {
                referenceNumber += "B";
            } else
            {
                referenceNumber += "N";
            }
        }
        
        client.setReferenceNumber(referenceNumber);         //sets the reference number to the object
    }

    public static void getDate(Contract client)
    {
        //Uses the system calender and the date formatting features to get the date the contract was taken out and sets it to the object.
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        client.setContractDate(sdf.format(cal.getTime()));
    }

    public static void saveToFile(Contract client)
    {
        //writes the properties of the object to the contracts file
        try
        {
            File fileOutput = new File("/nethome0/scm-studs/t7047098/jpr/ContractManager(Version 2)/build/generated-sources/ap-source-output/contracts.txt");
            try
            {
                FileWriter fw = new FileWriter(fileOutput,true);
                PrintWriter pw = new PrintWriter(fw);
                
                pw.print(client.getContractDate());
                pw.print("	");                 //gives an appropriate space between properties
                pw.print(client.getClientsPackage());
                pw.print("	");
                pw.print(client.getDataBundle());
                pw.print("	");
                pw.print(client.getPeriodOfContract());
                pw.print("	");
                pw.print(client.getInternationalCalls());
                pw.print("	");
                pw.print(client.getReferenceNumber());
                pw.print("	");
                pw.print(client.getMonthlyCharge());
                pw.print("	");
                pw.print(client.getClientName());
                pw.println();
                
                pw.close();
                fw.close();
            } catch (IOException e)
            {
                System.out.println(e);
            }
        } catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public static void generateSummary(Contract client, char businessAccount, double discountValue)
    {
        //Generates a summary that is always the same length and width dependant on the information from the contract.
        System.out.println(String.format("+%-44s+", "").replace(" ", "-"));
        System.out.println(String.format("|%-44s|", ""));
        System.out.println(String.format("| %-43s|", "Customer: " + client.getClientName()));
        System.out.println(String.format("|%-44s|", ""));
        System.out.println(String.format("|      %-38s|", "Ref: " + client.getReferenceNumber() + "         Date: " + client.getContractDate()));
        if (client.getClientsPackage() == 1 && client.getDataBundle() == 1)
        {
            System.out.println(String.format("|  %-42s|", "Package: Small (300)    Data: Low (1GB)"));
        } else if (client.getClientsPackage() == 1 && client.getDataBundle() == 2)
        {
            System.out.println(String.format("|  %-42s|", "Package: Small (300)    Data: Medium (4GB)"));
        } else if (client.getClientsPackage() == 1 && client.getDataBundle() == 3)
        {
            System.out.println(String.format("|  %-42s|", "Package: Small (300)    Data: High (8GB)"));
        } else if (client.getClientsPackage() == 2 && client.getDataBundle() == 1)
        {
            System.out.println(String.format("|  %-42s|", "Package: Medium (600)   Data: Low (1GB)"));
        } else if (client.getClientsPackage() == 2 && client.getDataBundle() == 2)
        {
            System.out.println(String.format("|  %-42s|", "Package: Medium (600)   Data: Medium (4GB)"));
        } else if (client.getClientsPackage() == 2 && client.getDataBundle() == 3)
        {
            System.out.println(String.format("|  %-42s|", "Package: Medium(600)    Data: High (8GB)"));
        } else if (client.getClientsPackage() == 3 && client.getDataBundle() == 1)
        {
            System.out.println(String.format("|  %-42s|", "Package: Large  (1200)  Data: Low (1GB)"));
        } else if (client.getClientsPackage() == 3 && client.getDataBundle() == 2)
        {
            System.out.println(String.format("|  %-42s|", "Package: large  (1200)  Data: Medium (4GB)"));
        } else if (client.getClientsPackage() == 3 && client.getDataBundle() == 3)
        {
            System.out.println(String.format("|  %-42s|", "Package: Large  (1200)  Data: High (8GB)"));
        } else
        {
            System.out.println(String.format("|  %-42s|", "Package: large  (1200)  Data: Unlimited"));
        }

        if (businessAccount == 'Y')
        {
            System.out.println(String.format("|   %-41s|", "Period: " + client.getPeriodOfContract() + " Months      Type: Business"));
        } else
        {
            System.out.println(String.format("|   %-41s|", "Period: " + client.getPeriodOfContract() + "Months       Type: Non-Business"));
        }

        System.out.println(String.format("|%-44s|", ""));

        if (discountValue != 0)
        {
            if (client.getInternationalCalls() == 'Y')
            {
                System.out.println(String.format("| %-43s|", "Discount: " + (int) (discountValue * 100) + "%     Intl. Calls: Yes"));
            } else
            {
                System.out.println(String.format("| %-43s|", "Discount: " + (int) (discountValue * 100) + "%     Intl. Calls: No"));
            }
        } else if (client.getInternationalCalls() == 'Y')
        {
            System.out.println(String.format("| %-43s|", "Discount: None    Intl. Calls: Yes"));
        } else
        {
            System.out.println(String.format("| %-43s|", "Discount: None    Intl. Calls: No"));
        }

        System.out.println(String.format("|%-44s|", ""));

        if (discountValue != 0)
        {
            System.out.print(String.format("|%38s", "Discounted Monthly Charge: £" + (double) client.getMonthlyCharge() / 100));
            System.out.println(String.format("%6s|", ""));
        } else
        {
            System.out.println(String.format("|%32s", "Monthly Charge: £" + (double) client.getMonthlyCharge() / 100) + String.format("%12s|", ""));
        }

        System.out.println(String.format("+%-44s+", "").replace(" ", "-"));

    }

    public static void summaryOfContract(Contract client)
    {
        Scanner keyboard = new Scanner(System.in);
        ArrayList<Contract> clients = new ArrayList();
        int totalNumber = 0, totalPrice = 0;

        clients = fileToReadFunction(clients, client);      //checks which file the user wants to read from and handles reading from the file

        System.out.println("Total Number of contracts: " + clients.size());

        //tallys up the number of large or unlimited data bundles that have been taken out
        for (int i = 0; i < clients.size(); i++)
        {
            if (clients.get(i).getDataBundle() == 3 || clients.get(i).getDataBundle() == 4)
            {
                totalNumber += 1;
            }
        }

        System.out.println("Contracts with High or Unlimited Data Bundles: " + totalNumber);
        totalNumber = 0;

        //tallys up the overal charge of large packages that have been taken out and how many there is.
        for (int i = 0; i < clients.size(); i++)
        {
            if (clients.get(i).getClientsPackage() == 3)
            {
                totalPrice += clients.get(i).getMonthlyCharge();
                totalNumber += 1;
            }
        }

        System.out.println("Average charge for large packages: " + (double) (totalPrice / totalNumber) / 100);
        System.out.println("");
        displayMonthContracts(clients);
    }

    public static ArrayList fileToReadFunction(ArrayList clients, Contract client)
    {
        Scanner keyboard = new Scanner(System.in);
        int fileToRead = 0;

        //Checks the file which the user wants to read from and sets the file path to pass the procedure which reads the file. 
        while (fileToRead != 1 && fileToRead != 2)
        {
            System.out.println("Please choose the file to open.");
            System.out.println("1) Contracts file");
            System.out.println("2) Archieve file");
            fileToRead = keyboard.nextInt();

            if (fileToRead == 1)
            {
                File file = new File("/nethome0/scm-studs/t7047098/jpr/ContractManager(Version 2)/build/generated-sources/ap-source-output/contracts.txt");
               clients = readFromFile(client, file);
            } else if (fileToRead == 2)
            {
                File file = new File("/nethome0/scm-studs/t7047098/jpr/ContractManager(Version 2)/build/generated-sources/ap-source-output/archive.txt");
                clients = readFromFile(client, file);
            } else
            {
                System.out.println("Please enter 1 or 2");
            }
        }
        return clients;
    }

    public static void displayMonthContracts(ArrayList<Contract> clients)
    {
        int[] monthTotal =
        {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        
        System.out.println("Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec");

        //sets the value for how many contracts where taken out each month
        for (int i = 0; i < clients.size(); i++)
        {
            switch (clients.get(i).getContractDate().substring(3, 6))
            {
                case "Jan":
                    monthTotal[0] += 1;
                    break;
                case "Feb":
                    monthTotal[1] += 1;
                    break;
                case "Mar":
                    monthTotal[2] += 1;
                    break;
                case "Apr":
                    monthTotal[3] += 1;
                    break;
                case "May":
                    monthTotal[4] += 1;
                    break;
                case "Jun":
                    monthTotal[5] += 1;
                    break;
                case "Jul":
                    monthTotal[6] += 1;
                    break;
                case "Aug":
                    monthTotal[7] += 1;
                    break;
                case "Sep":
                    monthTotal[8] += 1;
                    break;
                case "Oct":
                    monthTotal[9] += 1;
                    break;
                case "Nov":
                    monthTotal[10] += 1;
                    break;
                case "Dec":
                    monthTotal[11] += 1;
                    break;
                default:
                    System.out.println("Month not regonised");
            }
        }

        for (int i = 0; i < 12; i++)
        {
            System.out.print(String.format("%3s ", monthTotal[i]));
        }

        System.out.println("");
        System.out.println("");
    }

    public static ArrayList readFromFile( Contract client, File file)
    {
        Scanner input = null;
        ArrayList<Contract> clients = new ArrayList();

        //reads the values to the object and then sets the object to a array list and handles any errors that may occur
        try
        {
            input = new Scanner(file);  //Add file here
        } catch (FileNotFoundException e)
        {
            System.out.println(e);
        }

        try
        {
            while (input.hasNext())
            {
                client.setContractDate(input.next());
                client.setClientsPackage(input.nextInt());
                client.setDataBundle(input.nextInt());
                client.setPeriodOfContract(input.nextInt());
                client.setInternationalCalls(input.next().charAt(0));
                client.setReferenceNumber(input.next());
                client.setMonthlyCharge(input.nextInt());
                client.setClientName(input.next() + " " + input.next());
                clients.add(client);
                client = new Contract();
            }
        } catch (NullPointerException e)
        {
            System.out.println(e);
        }
        return clients;
    }

    public static void summaryOfMonthlyContract(Contract client)
    {
        Scanner keyboard = new Scanner(System.in);
        ArrayList<Contract> clients = new ArrayList();
        int totalNumContracts = 0, totalPrice = 0, totalBundles = 0, totalLargePackages = 0;
        int searchMonth = 0;
        String search = null;
        boolean valid = false;

        clients = fileToReadFunction(clients, client);
        
        //Checks which month the user would like to view
        while (!valid)
        {
            try
            {
                System.out.println("Which month would you like to view.");
                System.out.println("Please enter the number for the month you would like to view");
                searchMonth = keyboard.nextInt();
                
                switch (searchMonth)
                {
                    case 1:
                        search = "Jan";
                        valid = true;
                        break;
                    case 2:
                        search = "Feb";
                        valid = true;
                        break;
                    case 3:
                        search = "Mar";
                        valid = true;
                        break;
                    case 4:
                        search = "Apr";
                        valid = true;
                        break;
                    case 5:
                        search = "May";
                        valid = true;
                        break;
                    case 6:
                        search = "Jun";
                        valid = true;
                        break;
                    case 7:
                        search = "Jul";
                        valid = true;
                        break;
                    case 8:
                        search = "Aug";
                        valid = true;
                        break;
                    case 9:
                        search = "Sep";
                        valid = true;
                        break;
                    case 10:
                        search = "Oct";
                        valid = true;
                        break;
                    case 11:
                        search = "Nov";
                        valid = true;
                        break;
                    case 12:
                        search = "Dec";
                        valid = true;
                        break;
                    default:
                        System.out.println("Please enter a value between 1 and 12.");
                        valid = false;
                        break;
                }
            } catch (Exception e)
            {
                System.out.println("Please enter a Numerical Value");
            }
        }
        
        //Calculates the amount of contracts for that month, the amount of large or unlimated data bundles and the average price for a large package
        for (int i = 0; i < clients.size(); i++)
        {
            if (clients.get(i).getContractDate().substring(3, 6).equals(search))
            {
                totalNumContracts += 1;
                if (clients.get(i).getDataBundle() == 3 || clients.get(i).getDataBundle() == 4)
                {
                    totalBundles += 1;
                }

                if (clients.get(i).getClientsPackage() == 3)
                {
                    totalPrice += clients.get(i).getMonthlyCharge();
                    totalLargePackages += 1;
                }
            }
        }

        System.out.println("Total Number of contracts: " + totalNumContracts);
        System.out.println("Contracts with High or Unlimited Data Bundles: " + totalBundles);
        System.out.println("Average charge for large packages: " + (double) ((totalPrice / totalLargePackages) / 100));
        System.out.println("");
    }

    public static void findDisplayContract(Contract client)
    {
        Scanner keyboard = new Scanner(System.in);
        ArrayList<Contract> clients = new ArrayList();

        clients = fileToReadFunction(clients, client);

        System.out.println("Please enter what you would like to search.");
        String search = keyboard.nextLine();

        //checks if any of the contracts read from the file contain the search characters 
        for (int i = 0; i < clients.size(); i++)
        {
            if (clients.get(i).getClientName().contains(search) || clients.get(i).getReferenceNumber().contains(search))
            {
                searchedContact(clients.get(i));
            }
        }
    }

    public static void searchedContact(Contract client)
    {
        double discount = 0;
        char businessAccount = 'a';

        // uses the data from the contract retrieved to calculate the discount and whether they are a business account and then generates a summary for the information.
        if (client.getReferenceNumber().charAt(5) == 'B')
        {
            businessAccount = 'Y';
        } else
        {
            businessAccount = 'N';
        }

        discount = calculateDiscount(client.getPeriodOfContract(), businessAccount, client.getInternationalCalls());

        generateSummary(client, businessAccount, discount);
    }
}