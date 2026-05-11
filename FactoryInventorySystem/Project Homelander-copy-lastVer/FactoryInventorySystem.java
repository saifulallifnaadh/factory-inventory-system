import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FactoryInventorySystem {

    static String fileName = "Homelander.txt";

    // ============================================================
    // METHOD TO SAVE FILE
    // ============================================================
    public static void saveFile(Stack<Product> productStack) {
        try {
            FileWriter fw = new FileWriter(fileName, false); 
            
            Stack<Product> tempStack = new Stack<>();

            // 1. Inventory -> Temp
            while (!productStack.isEmpty()) {
                tempStack.push(productStack.pop());
            }

            // 2. Temp -> Inventory + Write File
            while (!tempStack.isEmpty()) {
                Product p = tempStack.pop();
                
                fw.write(
                    p.getProductID() + ";" +
                    p.getProductName() + ";" +
                    p.getProductQuantity() + ";" +
                    p.getPrice() + ";" +
                    p.getCategory() + "\n"
                );

                productStack.push(p);
            }
            
            fw.close();
        } catch (IOException e) {
            System.out.println("Oops, error while saving file!");
        }
    }

    public static void main(String[] args) {
        Stack<Product> inventoryStack = new Stack<>(); 
        Stack<Product> recentActivityStack = new Stack<>();
        Stack<Product> tempStack = new Stack<>(); 

        Scanner in = new Scanner(System.in);

        // ============================================================
        // 1. LOAD DATA FROM FILE
        // ============================================================
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            Scanner fileScanner = new Scanner(f);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                StringTokenizer st = new StringTokenizer(line,";");
                
                String id = st.nextToken(); 
                String name = st.nextToken();
                int qty = Integer.parseInt(st.nextToken());
                double price = Double.parseDouble(st.nextToken());
                String cat = st.nextToken();

                Product p = new Product(id, name, qty, price, cat);
                
                inventoryStack.push(p);
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file, continuing...");
        }

        int choice = -1;

        while (choice != 0) {
            System.out.println("\n========================================");
            System.out.println("      Factory Inventory System ");
            System.out.println("========================================");
            System.out.println("1. Display Inventory");
            System.out.println("2. Add New Product");
            System.out.println("3. Remove Product ");
            System.out.println("4. Search Product "); 
            System.out.println("5. Update Product"); 
            System.out.println("6. View Recent Activity");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            try {
                choice = Integer.parseInt(in.nextLine());
            } catch (Exception e) {
                choice = -1;
            }

            // ========================================================
            // OPTION 1: DISPLAY
            // ========================================================
            if (choice == 1) {
                if (inventoryStack.isEmpty()) {
                    System.out.println("Inventory is empty.");
                } else {
                    System.out.println("\n--- PRODUCT LIST (STACK) ---");
                    System.out.println("-------------------------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-25s | %-8s | %-12s | %-20s |%n", 
                            "ID", "Product Name", "Qty", "Price", "Category");
                    System.out.println("-------------------------------------------------------------------------------------------");

                    while (!inventoryStack.isEmpty()) {
                        Product p = inventoryStack.pop(); 
                        System.out.printf("| %-10s | %-25s | %-8d | RM %-9.2f | %-20s |%n", 
                                          p.getProductID(), 
                                          p.getProductName(), 
                                          p.getProductQuantity(), 
                                          p.getPrice(),
                                          p.getCategory());
                        tempStack.push(p); 
                    }
                    System.out.println("-------------------------------------------------------------------------------------------");

                    while (!tempStack.isEmpty()) {
                        inventoryStack.push(tempStack.pop());
                    }
                }
                System.out.println("\n[Press Enter to return to Main Menu...]");
                in.nextLine();
            }

          // ========================================================
            // OPTION 2: ADD NEW PRODUCT
            // ========================================================
            else if (choice == 2) {
                boolean addLoop = true;

                while (addLoop) {
                   
                    System.out.print("\nEnter Product ID: ");
                    String id = in.nextLine();

                    boolean idExists = false; 
                    
                    while(!inventoryStack.isEmpty()){
                        Product p = inventoryStack.pop();
                        if(p.getProductID().equalsIgnoreCase(id)){
                            idExists = true;
                        }
                        tempStack.push(p);
                    }
                    while(!tempStack.isEmpty()){
                        inventoryStack.push(tempStack.pop());
                    }

                    if (idExists) {
                        System.out.println("This ID already exists.");
                        
                        boolean validResponse = false;
                        while (!validResponse) {
                            System.out.print("Try another ID? (Y/N): ");
                            String retry = in.nextLine();
                            if (retry.equalsIgnoreCase("Y")) validResponse = true;
                            else if (retry.equalsIgnoreCase("N")) {
                                validResponse = true;
                                addLoop = false;
                                System.out.println("Returning...");
                            }
                        }

                    } else {
                        // --- STEP 2: NAME VALIDATION (INI YANG BARU) ---
                        // Kita masuk sini sebab ID dah LULUS (unik).
                        // Sekarang kita nak check Nama pula.
                        
                        String name = "";
                        boolean nameUnique = false; // Flag untuk loop nama

                        while (!nameUnique) {
                            System.out.print("Product Name: ");
                            name = in.nextLine();

                            boolean nameExists = false;

                            // 1. Tuang Keluar & Check Nama
                            while (!inventoryStack.isEmpty()) {
                                Product p = inventoryStack.pop();
                                
                                // Check kalau nama sama (Case insensitive: "Simen" == "simen")
                                if (p.getProductName().equalsIgnoreCase(name)) {
                                    nameExists = true;
                                }
                                tempStack.push(p);
                            }

                            // 2. Tuang Balik (Wajib!)
                            while (!tempStack.isEmpty()) {
                                inventoryStack.push(tempStack.pop());
                            }

                            // 3. Keputusan
                            if (nameExists) {
                                System.out.println("Error: Product Name '" + name + "' already exists! Please use a different name.");
                            } else {
                                nameUnique = true; // Nama unik, boleh keluar dari loop ni
                            }
                        }

                        // --- STEP 3: MINTA DATA LAIN (SAMA MACAM BIASA) ---
                        System.out.print("Quantity: ");
                        int qty = Integer.parseInt(in.nextLine());
                        System.out.print("Price: ");
                        double price = Double.parseDouble(in.nextLine());
                        
                        // Category Validation
                        String cat = "";
                        boolean validCat = false;
                        while (!validCat) {
                            System.out.println("Category: 1.Raw Material, 2.Finished Product");
                            System.out.println("Choose (1/2): ");
                            try {
                                int catOpt = Integer.parseInt(in.nextLine());
                                if (catOpt == 1) { cat = "Raw Material"; validCat = true; }
                                else if (catOpt == 2) { cat = "Finished Product"; validCat = true; }
                                else System.out.println("Invalid choice.");
                            } catch (Exception e) {
                                System.out.println("Invalid number.");
                            }
                        }

                        // --- STEP 4: SAVE ---
                        Product newP = new Product(id, name, qty, price, cat);

                        inventoryStack.push(newP);
                        recentActivityStack.push(newP); 
                        saveFile(inventoryStack); 
                        System.out.println("Product added successfully!");
                        
                        // Tanya user nak tambah barang lagi ke tak...
                        boolean validResponse = false;
                        while (!validResponse) {
                            System.out.print("\nAdd another product? (Y/N): ");
                            String again = in.nextLine();
                            if (again.equalsIgnoreCase("Y")) validResponse = true;
                            else if (again.equalsIgnoreCase("N")) {
                                validResponse = true;
                                addLoop = false;
                                System.out.println("Returning to Main Menu...");
                            }
                        }
                    }
                }
            }

            // ========================================================
            // OPTION 3: REMOVE PRODUCT (WITH CONFIRMATION)
            // ========================================================
            else if (choice == 3) {
                if (inventoryStack.isEmpty()) {
                    System.out.println("Inventory is empty. Nothing to remove.");
                    System.out.println("\n[Press Enter to return to Main Menu...]");
                    in.nextLine();
                } else {
                    boolean removeLoop = true;
                    while (removeLoop) {
                        
                        // --- DISPLAY LIST (Sama macam kod asal) ---
                        System.out.println("\n--- LIST OF PRODUCTS AVAILABLE TO REMOVE ---");
                        System.out.println("-------------------------------------------------------------------------------------------");
                        System.out.printf("| %-10s | %-25s | %-8s | %-12s | %-20s |%n", 
                                "ID", "Product Name", "Qty", "Price", "Category");
                        System.out.println("-------------------------------------------------------------------------------------------");

                        while (!inventoryStack.isEmpty()) {
                            Product p = inventoryStack.pop(); 
                            System.out.printf("| %-10s | %-25s | %-8d | RM %-9.2f | %-20s |%n", 
                                                    p.getProductID(), 
                                                    p.getProductName(), 
                                                    p.getProductQuantity(), 
                                                    p.getPrice(),
                                                    p.getCategory());
                            tempStack.push(p); 
                        }
                        System.out.println("-------------------------------------------------------------------------------------------");

                        while (!tempStack.isEmpty()) {
                            inventoryStack.push(tempStack.pop());
                        }
                        // --------------------------------------------------

                        System.out.print("\nEnter Product ID to remove: ");
                        String removeId = in.nextLine();
                        
                        boolean found = false;
                        boolean deleted = false; // Flag baru: untuk tahu user jadi delete ke tak
                        Product removedProduct = null;

                        while (!inventoryStack.isEmpty()) {
                            Product p = inventoryStack.pop();
                            
                            // Check ID
                            if (p.getProductID().equalsIgnoreCase(removeId)) {
                                found = true; // ID wujud
                                
                                // --- BAHAGIAN BARU: CONFIRMATION ---
                                System.out.println("\nProduct Found: " + p.getProductName());
                                System.out.print("Are you sure you want to delete? (Y/N): ");
                                String confirm = in.nextLine();

                                if (confirm.equalsIgnoreCase("Y")) {
                                    // User confirm NAK delete
                                    deleted = true;
                                    removedProduct = p;
                                    recentActivityStack.push(p); 
                                    System.out.println("Product removed from inventory.");
                                    // KITA TAK PUSH KE TEMPSTACK (Biar hilang)
                                } else {
                                    // User CANCEL (tekan N atau lain-lain)
                                    System.out.println("Deletion cancelled. Product kept safe.");
                                    tempStack.push(p); // Simpan balik product ni
                                }

                            } else {
                                tempStack.push(p); // Product lain selamatkan macam biasa
                            }
                        }
                        
                        // Kemas balik barang yang terselamat
                        while (!tempStack.isEmpty()) {
                            inventoryStack.push(tempStack.pop());
                        }

                        // --- LOGIK LEPAS SEARCH ---
                        if (deleted) {
                            // Kalau betul-betul delete, baru save file
                            saveFile(inventoryStack);
                            System.out.println("Successfully updated database: " + removedProduct.getProductName() + " is gone.");
                            
                            // Tanya nak remove lagi tak (Sama flow macam kod asal)
                            boolean validResponse = false;
                            while (!validResponse) {
                                System.out.print("\nRemove another product? (Y = Yes / N = Back to Menu): ");
                                String again = in.nextLine();
                                if(again.equalsIgnoreCase("Y")) {
                                    validResponse = true;
                                } else if(again.equalsIgnoreCase("N")) {
                                    validResponse = true;
                                    removeLoop = false;
                                    System.out.println("Returning to Main Menu...");
                                } else {
                                    System.out.println(" Invalid input. Please enter 'Y' or 'N'.");
                                }
                            }

                        } else if (found && !deleted) {
                            // ID jumpa, tapi user cancel (tekan No)
                            // Kita tak save file, cuma tanya nak try ID lain ke atau menu
                            
                            boolean validResponse = false;
                            while (!validResponse) {
                                System.out.print("\nTry removing another product? (Y = Yes / N = Back to Menu): ");
                                String again = in.nextLine();
                                if(again.equalsIgnoreCase("Y")) {
                                    validResponse = true;
                                } else if(again.equalsIgnoreCase("N")) {
                                    validResponse = true;
                                    removeLoop = false;
                                    System.out.println("Returning to Main Menu...");
                                } else {
                                    System.out.println(" Invalid input. Please enter 'Y' or 'N'.");
                                }
                            }
                        
                        } else {
                            // ID Tak jumpa langsung
                            System.out.println(" Product ID not found.");
                            
                            boolean validResponse = false;
                            while (!validResponse) {
                                System.out.print("Do you want to try again? (Y = Yes / N = Back to Menu): ");
                                String retry = in.nextLine();
                                if (retry.equalsIgnoreCase("Y")) {
                                    validResponse = true;
                                } else if (retry.equalsIgnoreCase("N")) {
                                    validResponse = true;
                                    removeLoop = false; 
                                    System.out.println("Returning to Main Menu...");
                                } else {
                                    System.out.println(" Invalid input. Please enter 'Y' or 'N'.");
                                }
                            }
                        }
                    }
                }
            }

            // ========================================================
            // OPTION 4: SEARCH PRODUCT (UPDATED WITH PRICE)
            // ========================================================
            else if (choice == 4) {
                if (inventoryStack.isEmpty()) {
                    System.out.println("Inventory is empty. Nothing to search.");
                    System.out.println("\n[Press Enter to return to Main Menu...]");
                    in.nextLine();
                } else {
                    boolean searchLoop = true;
                    while (searchLoop) {
                        System.out.println("\n--- SEARCH MENU ---");
                        System.out.println("1. Search by ID");
                        System.out.println("2. Search by Product Name");
                        System.out.println("3. Search by Quantity");
                        System.out.println("4. Search by Price");    // NEW
                        System.out.println("5. Search by Category"); // MOVED
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter choice: ");

                        int subChoice = -1;
                        try {
                            subChoice = Integer.parseInt(in.nextLine());
                        } catch (Exception e) {
                            subChoice = -1;
                        }

                        if (subChoice == 0) {
                            searchLoop = false;
                            System.out.println("Returning to Main Menu...");
                        } 
                        else if (subChoice >= 1 && subChoice <= 5) { // Updated Range
                            String keyword = "";
                            
                            // Handling Category Selection (Now choice 5)
                            if (subChoice == 5) 
                            {
                                System.out.println("\nSelect Category to search:");
                                System.out.println("1. Raw Material");
                                System.out.println("2. Finished Product");
                                System.out.print("Enter choice (1/2): ");
                                try {
                                    int catOpt = Integer.parseInt(in.nextLine());
                                    if (catOpt == 1) keyword = "Raw Material";
                                    else if (catOpt == 2) keyword = "Finished Product";
                                    else keyword = "INVALID";
                                } catch (Exception e) {
                                    keyword = "INVALID";
                                }
                            } else {
                                System.out.print("Enter keyword to search: ");
                                keyword = in.nextLine();
                            }

                            boolean found = false;
                            System.out.println("\n--- SEARCH RESULTS (" + keyword + ") ---");
                            System.out.println("-------------------------------------------------------------------------------------------");
                            System.out.printf("| %-10s | %-25s | %-8s | %-12s | %-20s |%n", 
                                    "ID", "Product Name", "Qty", "Price", "Category");
                            System.out.println("-------------------------------------------------------------------------------------------");

                            while (!inventoryStack.isEmpty()) {
                                Product p = inventoryStack.pop();
                                boolean isMatch = false;
                                
                                if (subChoice == 1) { // ID
                                    if (p.getProductID().equalsIgnoreCase(keyword)) isMatch = true;
                                } else if (subChoice == 2) { // Name
                                    if (p.getProductName().toLowerCase().contains(keyword.toLowerCase())) isMatch = true;
                                } else if (subChoice == 3) { // Quantity
                                    if (String.valueOf(p.getProductQuantity()).equals(keyword)) isMatch = true;
                                } else if (subChoice == 4) { // Price (NEW)
                                    try {
                                        double targetPrice = Double.parseDouble(keyword);
                                        if (p.getPrice() == targetPrice) isMatch = true;
                                    } catch (NumberFormatException e) {
                                        // Ignore invalid number format, just won't match
                                    }
                                } else if (subChoice == 5) { // Category
                                    if (p.getCategory().equalsIgnoreCase(keyword)) isMatch = true;
                                }

                                if (isMatch) {
                                    System.out.printf("| %-10s | %-25s | %-8d | RM %-9.2f | %-20s |%n", 
                                            p.getProductID(), p.getProductName(), 
                                            p.getProductQuantity(), p.getPrice(), p.getCategory());
                                    found = true;
                                }
                                tempStack.push(p); 
                            }
                            System.out.println("-------------------------------------------------------------------------------------------");

                            while (!tempStack.isEmpty()) {
                                inventoryStack.push(tempStack.pop());
                            }

                            if (!found) {
                                System.out.println("No result found.");
                                
                                boolean validResponse = false;
                                while (!validResponse) {
                                    System.out.print("Do you want to search again? (Y/N): ");
                                    String retry = in.nextLine();
                                    if (retry.equalsIgnoreCase("Y")) {
                                        validResponse = true;
                                    } else if (retry.equalsIgnoreCase("N")) {
                                        validResponse = true;
                                        searchLoop = false;
                                        System.out.println("Returning to Main Menu...");
                                    } else {
                                        System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                                    }
                                }
                            } else {
                                boolean validResponse = false;
                                while (!validResponse) {
                                    System.out.print("Search another item? (Y/N): ");
                                    String retry = in.nextLine();
                                    if (retry.equalsIgnoreCase("Y")) {
                                        validResponse = true;
                                    } else if (retry.equalsIgnoreCase("N")) {
                                        validResponse = true;
                                        searchLoop = false;
                                        System.out.println("Returning to Main Menu...");
                                    } else {
                                        System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Invalid search option.");
                        }
                    }
                }
            }

            // ========================================================
            // OPTION 5: UPDATE PRODUCT
            // ========================================================
            else if (choice == 5) {
                
                if (inventoryStack.isEmpty()) {
                    System.out.println("Empty, nothing to update.");
                    System.out.println("\n[Press Enter to return to Main Menu...]");
                    in.nextLine();
                } else {
                    // PART 1: DISPLAY FULL TABLE
                    System.out.println("\n--- LIST OF PRODUCTS AVAILABLE FOR UPDATE ---");
                    System.out.println("-------------------------------------------------------------------------------------------");
                    System.out.printf("| %-10s | %-25s | %-8s | %-12s | %-20s |%n", 
                            "ID", "Product Name", "Qty", "Price", "Category");
                    System.out.println("-------------------------------------------------------------------------------------------");
                    
                    while (!inventoryStack.isEmpty()) {
                        Product p = inventoryStack.pop();
                        System.out.printf("| %-10s | %-25s | %-8d | RM %-9.2f | %-20s |%n", 
                                          p.getProductID(), 
                                          p.getProductName(), 
                                          p.getProductQuantity(), 
                                          p.getPrice(),
                                          p.getCategory());
                        tempStack.push(p);
                    }
                    while (!tempStack.isEmpty()) {
                        inventoryStack.push(tempStack.pop());
                    }
                    System.out.println("-------------------------------------------------------------------------------------------");

                    // PART 2: UPDATE LOGIC
                    boolean searchAgain = true;
                    while (searchAgain) {
                        System.out.print("Please enter Product ID to update (or type 'EXIT' to return): ");
                        String targetId = in.nextLine();

                        if (targetId.equalsIgnoreCase("EXIT")) 
                        {
                            searchAgain = false;
                            System.out.println("Returning to Main Menu...");
                            break;
                        }

                        boolean found = false;

                        while (!inventoryStack.isEmpty()) {
                            Product p = inventoryStack.pop();

                            if (p.getProductID().equalsIgnoreCase(targetId)) {
                                found = true;
                                boolean updatingThisProduct = true;

                                while (updatingThisProduct) {
                                    
                                    System.out.println("\n--- SELECTED PRODUCT DETAILS ---");
                                    System.out.println("-------------------------------------------------------------------------------------------");
                                    System.out.printf("| %-10s | %-25s | %-8d | RM %-9.2f | %-20s |%n", 
                                              p.getProductID(), 
                                              p.getProductName(), 
                                              p.getProductQuantity(), 
                                              p.getPrice(),
                                              p.getCategory());
                                    System.out.println("-------------------------------------------------------------------------------------------");

                                    System.out.println("WHAT DO YOU WANT TO UPDATE?");
                                    System.out.println("1. Update Name");
                                    System.out.println("2. Update Quantity");
                                    System.out.println("3. Update Price");
                                    System.out.println("4. Update Category");
                                    System.out.println("0. Back to Main Menu");
                                    System.out.print("Enter your choice (0-4): ");
                                    
                                    int updateChoice = -1;
                                    try {
                                        updateChoice = Integer.parseInt(in.nextLine());
                                    } catch (Exception e) {
                                        updateChoice = -1;
                                    }

                                    if (updateChoice == 0) {
                                        recentActivityStack.push(p); 
                                        updatingThisProduct = false;
                                        searchAgain = false; 
                                        System.out.println("Returning to Main Menu...");
                                        break; 
                                    }
                                    
                                    else if (updateChoice == 1) 
                                        {
                                            System.out.print("Enter New Name: ");
                                            String newName = in.nextLine();
                                            p.setProductName(newName);
                                            System.out.println("Name updated!");
                                        } 
                                    else if (updateChoice == 2) 
                                        {
                                        boolean validQty = false;
                                            while (!validQty) 
                                            {
                                              System.out.print("Enter New Quantity: ");
                                                try {
                                                    int newQty = Integer.parseInt(in.nextLine());
                                                    if (newQty < 0) 
                                                    {
                                                        System.out.println("Quantity cannot be negative. Please try again.");
                                                    }
                                                    else 
                                                    {
                                                        p.setProductQuantity(newQty);
                                                        System.out.println("Quantity updated!");
                                                        validQty = true;
                                                    }
                                            } 
                                            catch (Exception e) 
                                                {
                                                    System.out.println("Invalid input. Please enter an integer.");
                                                }
                                        }
                                    } 
                                    else if (updateChoice == 3) {
                                        boolean validPrice = false;
                                        while (!validPrice) {
                                            System.out.print("Enter New Price: ");
                                            try {
                                                double newPrice = Double.parseDouble(in.nextLine());
                                                if (newPrice < 0) 
                                                    {
                                                        System.out.println("Price cannot be negative. Please try again.");
                                                    }
                                                 
                                                else 
                                                    {
                                                        p.setPrice(newPrice);
                                                        System.out.println("Price updated!");
                                                        validPrice = true;
                                                    }
                                            } catch (Exception e) {
                                                System.out.println("Invalid input. Please enter a number.");
                                            }
                                        }

                                    } else if (updateChoice == 4) {
                                        System.out.println("Select New Category:");
                                        System.out.println("1. Raw Material");
                                        System.out.println("2. Finished Product");
                                        System.out.print("Choose (1/2): ");
                                        int catOpt = Integer.parseInt(in.nextLine());
                                        if (catOpt == 1) p.setCategory("Raw Material");
                                        else if (catOpt == 2) p.setCategory("Finished Product");
                                        System.out.println("Category updated!");

                                    } else {
                                        System.out.println("Invalid choice.");
                                    }
                                    
                                    if (updateChoice != 0) {
                                        boolean validResponse = false;
                                        while (!validResponse) {
                                            System.out.print("\nDo you want to continue updating this product? (Y = Yes / N = Back to Menu): ");
                                            String cont = in.nextLine();
                                            
                                            if (cont.equalsIgnoreCase("Y")) {
                                                validResponse = true;
                                            } else if (cont.equalsIgnoreCase("N")) 
                                                {
                                                    validResponse = true;
                                                    updatingThisProduct = false; 
                                                    recentActivityStack.push(p); 
                                                    searchAgain = false; 
                                                    System.out.println("Returning to Main Menu...");
                                                } 
                                            else 
                                                {
                                                    System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                                                }
                                        }
                                    }
                                }
                            }
                            tempStack.push(p); 
                        }

                        while (!tempStack.isEmpty()) {
                            inventoryStack.push(tempStack.pop());
                        }

                        if (found) 
                            {
                                saveFile(inventoryStack);
                            }
                        else 
                            {
                                System.out.println("Product ID not found.");
                            
                                boolean validResponse = false;
                                while (!validResponse) {
                                    System.out.print("Try another ID? (Y = Yes / N = Back to Menu): ");
                                    String answer = in.nextLine();
                                    if (answer.equalsIgnoreCase("Y")) 
                                    {
                                        validResponse = true;
                                    } 
                                    else if (answer.equalsIgnoreCase("N")) 
                                    {
                                        validResponse = true;
                                        searchAgain = false; 
                                        System.out.println("Returning to Main Menu...");
                                    }
                                 
                                    else 
                                    {
                                        System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                                    }
                                }
                        }
                    }
                }
            }

            // ========================================================
            // OPTION 6: VIEW TRANSACTION
            // ========================================================
            else if (choice == 6) {
                if (recentActivityStack.isEmpty()) 
                    {
                        System.out.println("No recent activity recorded.");
                    } 
                else 
                    {
                        System.out.println("\n--- --- INVENTORY UPDATES LOG --- ---");
                        while (!recentActivityStack.isEmpty()) 
                        {
                            Product p = recentActivityStack.pop();
                            System.out.println("Product: " + p.getProductName() + " (Last Edited)");
                            tempStack.push(p);
                        }
                        while (!tempStack.isEmpty())   
                        {
                            recentActivityStack.push(tempStack.pop());
                        }
                    }
                System.out.println("\n[Press Enter to return to Main Menu...]");
                in.nextLine();
            }

            else if (choice == 0) 
                {
                    System.out.println("Bye bye! Thank you.");
                } 
            else 
                {
                    System.out.println("Invalid number selected.");
                }
        }
    }
}