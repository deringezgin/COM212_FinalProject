import mainStructures.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

// COM212 Data Structures | Spring 2024 | Final Project
// Derin Gezgin | Dimitris Seremetis | Johnny Andreasen | Nick Essery
// Main Program to run the GUI. It has different functions for the different windows of the program.
// Run the main function to start

public class ParkerFilmsGUI implements Serializable {
    private static final long serialVersionUID = 12345678910L;
    private static final Font titleFont = new Font("Verdana", Font.BOLD, 40);
    private static final Font textFont = new Font("Verdana", Font.PLAIN, 20);
    private static final Font boldTextFont = new Font("Verdana", Font.BOLD, 20);
    private static final Font buttonFont = new Font("Verdana", Font.BOLD, 20);
    private static final Font subTitleFont = new Font("Verdana", Font.PLAIN, 30);
    private static final Font boldSubTitleFont = new Font("Verdana", Font.BOLD, 30);
    public static final Color VERY_LIGHT_BLUE = new Color(40, 165, 255);
    private CustomerStorage customers;
    private Wishlist wishlist;
    private HaveWatched haveWatched;
    private MovieScoresHeap movieScoresHeap;
    private MoviesByID moviesByID;
    private MoviesByDate moviesByDate;
    private JPanel panel;

    private JFrame frame;

    public ParkerFilmsGUI() {
        // Loading data
        loadData();
    }

    //////////////////// CUSTOMER SIDE OF THE PROGRAM //////////////////////////////////////////////////////////////////

    private void customerMenu(Customer customer) {
        // Customer menu that has the different options for the customer
        panel.removeAll();  // Clear the screen
        frame.setTitle(customer.getName() + " - Customer Menu");

        // Setting the title of the windwo
        JLabel adminTitleLabel = new JLabel("Welcome Back " + customer.getName(), JLabel.CENTER);
        adminTitleLabel.setFont(titleFont);
        panel.add(adminTitleLabel, BorderLayout.NORTH);

        // Setting up buttons and creating a button panel
        JButton accessMoviesByID = new JButton("Access Movie by ID or Release Date");  // Creating buttons
        JButton accessWishlist = new JButton("Access Wishlist");
        JButton accessHaveWatched = new JButton("Access the Watched Movies List");
        JButton printMoviesByDate = new JButton("View the movies in order of release date");
        JButton changeCustomerPassword = new JButton("Edit Customer Information");
        JButton goMainMenu = new JButton("Logout to the main menu");
        accessMoviesByID.setFont(boldTextFont);  // Setting the textFont
        accessWishlist.setFont(boldTextFont);
        printMoviesByDate.setFont(boldTextFont);
        goMainMenu.setFont(boldTextFont);
        changeCustomerPassword.setFont(boldTextFont);
        accessHaveWatched.setFont(boldTextFont);
        JPanel buttonPanel = new JPanel();  // Creating a panel for storing the buttons
        buttonPanel.setBackground(panel.getBackground());  // Using the parent background
        buttonPanel.setLayout(new GridLayout(6, 1, 0, 0));
        buttonPanel.add(accessMoviesByID);
        buttonPanel.add(accessWishlist);
        buttonPanel.add(accessHaveWatched);
        buttonPanel.add(printMoviesByDate);
        buttonPanel.add(changeCustomerPassword);
        buttonPanel.add(goMainMenu);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // If the button is clicked run the method that creates the screen with the access movies by id interface
        accessMoviesByID.addActionListener(e -> accessMoviesByIDorReleaseDateCustomer(customer));

        // If the button is clicked run the method that creates the screen with the wishlist access interface
        accessWishlist.addActionListener(e -> accessWishlist(customer));

        // If the button is clicked run the method that creates the screen with the wishlist access interface
        accessHaveWatched.addActionListener(e -> accessHaveWatched(customer));

        // If the button is clicked, run the method that creates the screen that prints the movies by release date
        printMoviesByDate.addActionListener(e -> viewByReleaseDateCustomer(customer));

        changeCustomerPassword.addActionListener(e -> editCustomerInformation(customer));

        // If the button is clicked, it returns to the main menu.
        goMainMenu.addActionListener(e -> loginMenu("Customer"));

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void accessMoviesByIDorReleaseDateCustomer(Customer customer) {
        panel.removeAll();  // Clearing the screen
        frame.setTitle(customer.getName() + " - Access Movies by ID or Release Date");

        // Title label
        JLabel titleLabel = new JLabel("Access Movies By ID or Release Date", JLabel.CENTER);
        titleLabel.setFont(boldSubTitleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Search Panel for saving the input field, button and text fields
        JPanel searchPanel = new JPanel(new GridLayout(7, 2, 20, 0));
        searchPanel.setBackground(panel.getBackground());

        // Text-field for entering the movieID
        JTextField movieIdField = new JTextField(10);
        movieIdField.setFont(textFont);

        JTextField movieDateField = new JTextField(10);
        movieDateField.setFont(textFont);

        // Setting up the search button and movie labels
        JButton searchButtonID = new JButton("Search Movie by ID");
        JButton searchButtonDate = new JButton("Search Movie by Date");
        JLabel movieTitleLabel = new JLabel("Title: ");
        JLabel movieReleaseDateLabel = new JLabel("Release Date: ");
        JLabel movieIDLabel = new JLabel("ID: ");
        JLabel movieRatingLabel = new JLabel("Rating: ");
        JLabel movieAvailabilityLabel = new JLabel("Availability: ");
        JLabel empty = new JLabel("");
        JButton addWishlistButton = new JButton("Add Movie to the Wishlist");
        JButton addHaveWatchedButton = new JButton("Add Movie to the Have Watched");
        searchButtonID.setFont(boldTextFont);  // Setting the font
        searchButtonDate.setFont(boldTextFont);
        movieTitleLabel.setFont(boldTextFont);
        movieReleaseDateLabel.setFont(boldTextFont);
        movieIDLabel.setFont(boldTextFont);
        movieRatingLabel.setFont(boldTextFont);
        movieAvailabilityLabel.setFont(boldTextFont);
        addWishlistButton.setFont(buttonFont);
        addHaveWatchedButton.setFont(buttonFont);
        addWishlistButton.setPreferredSize(new Dimension(250, 40)); // Setting the size of the buttons
        addHaveWatchedButton.setPreferredSize(new Dimension(250, 40));

        // Adding the button and the text fields to the searchPanel
        searchPanel.add(movieIdField);
        searchPanel.add(searchButtonID);
        searchPanel.add(movieDateField);
        searchPanel.add(searchButtonDate);
        searchPanel.add(movieTitleLabel);
        searchPanel.add(movieReleaseDateLabel);
        searchPanel.add(movieIDLabel);
        searchPanel.add(movieRatingLabel);
        searchPanel.add(movieAvailabilityLabel);
        searchPanel.add(empty);
        panel.add(searchPanel, BorderLayout.CENTER);  // Adding the search panel to the main panel

        // Creating a back button for going back to the customer menu
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> customerMenu(customer));

        clearActionListener(searchButtonID);
        searchButtonID.addActionListener(e -> {
            // Search button that looks for a movie by id
            String movieId = movieIdField.getText();
            movieDateField.setText("");
            movieIdField.setText("");
            if (intValidation(movieId, 10000, 99999)) {  // Checking if the input is a valid integer in the range
                int movieIDInt = Integer.parseInt(movieId);
                Movie foundMovie = moviesByID.searchMovieByID(movieIDInt);
                if (foundMovie != null) {  // If the movie is found, update the text fields and show the details
                    movieTitleLabel.setText("Title: " + foundMovie.getTitle());
                    movieReleaseDateLabel.setText("Release Date: " + foundMovie.convertToDate());
                    movieRatingLabel.setText("Rating: " + foundMovie.getScore());
                    movieIDLabel.setText("ID: " + foundMovie.getID());
                    movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));
                    searchPanel.add(addWishlistButton);  // Also show the buttons as options
                    searchPanel.add(addHaveWatchedButton);

                    clearActionListener(addWishlistButton);
                    addWishlistButton.addActionListener(e12 -> {
                        // If the add wishlist button is clicked, add movie to the wishlist and update the wishlist
                        if (customer.getWishlist().addMovie(foundMovie)) {
                            // If the wishlist is not full and successfully added the movie,
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " added to wishlist");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Can't add " + foundMovie.getTitle() + " to the wishlist.\nWishlist is full!");
                        }
                        saveData();
                        accessMoviesByIDorReleaseDateCustomer(customer);
                    });

                    clearActionListener(addHaveWatchedButton);
                    addHaveWatchedButton.addActionListener(e1 -> {
                        // If the have watched button is clicked, add the movie and update the have watched
                        if (customer.getWatchedList().searchMovieByID(movieIDInt) == null) {  // If the movie is not in the have watched
                            customer.getWatchedList().insertMovie(foundMovie);
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " added to the Watched List");
                        } else {
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " is already in the Watched List");
                        }
                        saveData();
                        accessMoviesByIDorReleaseDateCustomer(customer);
                    });
                } else {
                    // If the movie is not found
                    JOptionPane.showMessageDialog(panel, "No Movie Found with the specific movie ID");
                }
            } else {
                // If the input is invalid
                JOptionPane.showMessageDialog(panel, "Please enter a valid movie ID (10000 - 99999)");
            }
        });

        clearActionListener(searchButtonDate);
        searchButtonDate.addActionListener(e -> {
            // Search button that looks for a movie by id
            String movieDate = movieDateField.getText();
            movieDateField.setText("");
            movieIdField.setText("");

            if (intValidation(movieDate, 10000101, 99999999)) {  // Checking if the input is a valid integer in the range
                int movieDateInt = Integer.parseInt(movieDate);
                Movie foundMovie = moviesByDate.searchMovieByDate(movieDateInt);
                if (foundMovie != null) {  // If the movie is found, update the text fields and show the details
                    movieTitleLabel.setText("Title: " + foundMovie.getTitle());
                    movieReleaseDateLabel.setText("Release Date: " + foundMovie.convertToDate());
                    movieRatingLabel.setText("Rating: " + foundMovie.getScore());
                    movieIDLabel.setText("ID: " + foundMovie.getID());
                    movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));
                    searchPanel.add(addWishlistButton);  // Also show the buttons as options
                    searchPanel.add(addHaveWatchedButton);

                    clearActionListener(addWishlistButton);
                    addWishlistButton.addActionListener(e12 -> {
                        // If the add wishlist button is clicked, add movie to the wishlist and update the wishlist
                        if (customer.getWishlist().addMovie(foundMovie)) {
                            // If the wishlist is not full and successfully added the movie,
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " added to wishlist");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Can't add " + foundMovie.getTitle() + " to the wishlist.\nWishlist is full!");
                        }
                        saveData();
                        accessMoviesByIDorReleaseDateCustomer(customer);
                    });

                    clearActionListener(addHaveWatchedButton);
                    addHaveWatchedButton.addActionListener(e1 -> {
                        // If the have watched button is clicked, add the movie and update the have watched
                        if (customer.getWatchedList().searchMovieByDate(movieDateInt) == null) {
                            customer.getWatchedList().insertMovie(foundMovie);
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " added to Watched List");
                        } else {
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " is already in the Watched List");
                        }
                        saveData();
                        accessMoviesByIDorReleaseDateCustomer(customer);
                    });
                } else {
                    // If the movie is not found
                    JOptionPane.showMessageDialog(panel, "No Movie Found with the specific release Date");
                }
            } else {
                // If the input is invalid
                JOptionPane.showMessageDialog(panel, "Please enter a valid Release Date (YYYYMMDD)");
            }
        });


        panel.add(backButton, BorderLayout.SOUTH);

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void accessWishlist(Customer customer) {
        // Function to remove the least rated movie from the system
        panel.removeAll();  // Clearing the screen
        frame.setTitle(customer.getName() + " - Access Wishlist");

        // Setting up the screen title
        JLabel leastRatedTitleLabel = new JLabel("Access Wishlist", JLabel.CENTER);
        leastRatedTitleLabel.setFont(titleFont);
        panel.add(leastRatedTitleLabel, BorderLayout.NORTH);

        // Saving the first movie in the wishlist in a variable
        Movie firstMovie = customer.getWishlist().getFirstMovie();
        if (firstMovie != null) {  // If the least rated movie is not null (not empty)
            JPanel movieInfoPanel = new JPanel(new GridLayout(5, 1));  // Create a new panel for displaying the information of the movie
            movieInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            movieInfoPanel.setBackground(panel.getBackground());

            // Creating fields for the attributes of the movie
            JLabel titleLabel = new JLabel("Title: " + firstMovie.getTitle(), JLabel.CENTER);
            JLabel releaseDateLabel = new JLabel("Release Date: " + firstMovie.convertToDate(), JLabel.CENTER);
            JLabel idLabel = new JLabel("ID: " + firstMovie.getID(), JLabel.CENTER);
            JLabel ratingLabel = new JLabel("Rating: " + firstMovie.getScore(), JLabel.CENTER);
            JLabel availabilityLabel = new JLabel("Availability: " + getAvailabilityText(firstMovie.getAvailability()), JLabel.CENTER);
            titleLabel.setFont(boldSubTitleFont);  // Setting the font
            releaseDateLabel.setFont(boldSubTitleFont);
            idLabel.setFont(boldSubTitleFont);
            ratingLabel.setFont(boldSubTitleFont);
            availabilityLabel.setFont(boldSubTitleFont);
            movieInfoPanel.add(titleLabel);  // Adding the elements to the panel
            movieInfoPanel.add(releaseDateLabel);
            movieInfoPanel.add(idLabel);
            movieInfoPanel.add(ratingLabel);
            movieInfoPanel.add(availabilityLabel);
            panel.add(movieInfoPanel, BorderLayout.CENTER);  // Adding the infoPanel to the main panel

            if (firstMovie.getAvailability()) {
                // Adding the buttons
                JButton removeButton = new JButton("Remove the Movie");
                JButton backButton = new JButton("Go Back");
                removeButton.setFont(buttonFont);
                backButton.setFont(buttonFont);

                // Action listener for removeButton
                clearActionListener(removeButton);
                removeButton.addActionListener(e -> {
                    // If the user choose to remove the least rated movie
                    // Ask if they want to add it to the watched-list
                    Movie movie = customer.getWishlist().getFirstMovie();
                    customer.getWishlist().deleteFirstMovie();
                    int dialogResult = JOptionPane.showConfirmDialog(panel, "You're removing the movie from the wishlist. Would you like to add it to the have watched?", "Add to Watched-List", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        if (customer.getWatchedList().searchMovieByID(movie.getID()) == null) {
                            customer.getWatchedList().insertMovie(movie);
                            JOptionPane.showMessageDialog(panel, movie.getTitle() + " has been removed from customer wishlist.\nIt's added to the watched-list.");
                        } else {
                            JOptionPane.showMessageDialog(panel, movie.getTitle() + " has been removed from customer wishlist.\nIt's already in the watched-list.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, movie.getTitle() + " has been removed from customer wishlist.");
                    }
                    saveData();
                    accessWishlist(customer);
                });

                // If back-button is clicked, return to the adminMenu
                backButton.addActionListener(e -> customerMenu(customer));

                // Adding the buttons to the screen
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center align the buttons
                buttonPanel.add(backButton);
                buttonPanel.add(removeButton);

                buttonPanel.setBackground(panel.getBackground());
                panel.add(buttonPanel, BorderLayout.SOUTH);
            } else {
                panel.removeAll();
                customer.getWishlist().deleteFirstMovie();
                saveData();
                JOptionPane.showMessageDialog(panel, firstMovie.getTitle() + " is not available.\nIt'll be removed from the system.");
                accessWishlist(customer);
            }

        } else {
            // If there are no movies in the system, prints a message
            JLabel noMoviesLabel = new JLabel("No available movies to show", JLabel.CENTER);
            noMoviesLabel.setFont(titleFont);
            panel.add(noMoviesLabel, BorderLayout.CENTER);

            // Adding a back button that'd return to the adminMenu
            JButton backButton = new JButton("Go Back");
            backButton.setFont(textFont);
            backButton.addActionListener(e -> customerMenu(customer));
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(backButton);
            buttonPanel.setBackground(panel.getBackground());
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void accessHaveWatched(Customer customer) {
        // Function to print the movies by release date on the screen
        panel.removeAll();  // Clearing the screen
        frame.setTitle(customer.getName() + " - Access Have Watched");

        // Adding the title label
        JLabel titleLabel = new JLabel("Customer Have Watched List", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create a table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Movie Name");
        tableModel.addColumn("Release Date");
        tableModel.addColumn("ID");
        tableModel.addColumn("Score");
        tableModel.addColumn("Availability");

        getCustomerHaveWatched(customer.getWatchedList(), tableModel);

        // Creating the table
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {  // Being sure that the cells are not editable
                return false;
            }
        };

        table.setFont(new Font("Verdana", Font.PLAIN, 15));
        table.setRowHeight(25);

        // Setting column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(50);

        // Adding a scrollPane to the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300)); // Set a fixed size
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(panel.getBackground());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> customerMenu(customer));
        buttonPanel.add(backButton);

        // Search Button
        JButton searchButton = new JButton("Search in the Have Watched");
        searchButton.setFont(buttonFont);
        buttonPanel.add(searchButton);
        searchButton.addActionListener(e -> searchInHaveWatched(customer));

        // Adding buttons for wishlist and watched
        JButton addToWishlistButton = new JButton("Add to Wishlist");
        addToWishlistButton.setFont(buttonFont);
        addToWishlistButton.setVisible(false);
        buttonPanel.add(addToWishlistButton);

        JButton removeMovieButton = new JButton("Remove Movie");
        removeMovieButton.setFont(buttonFont);
        removeMovieButton.setVisible(false);
        buttonPanel.add(removeMovieButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        ListSelectionModel selectionModel = table.getSelectionModel();  // When a row is selected
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Clear all action listeners from the buttons
                for (ActionListener listener : removeMovieButton.getActionListeners()) {
                    removeMovieButton.removeActionListener(listener);
                }
                for (ActionListener listener : addToWishlistButton.getActionListeners()) {
                    addToWishlistButton.removeActionListener(listener);
                }

                if (table.getSelectedRow() != -1) {
                    addToWishlistButton.setVisible(true);  // Show the option buttons
                    removeMovieButton.setVisible(true);

                    Object id = table.getValueAt(table.getSelectedRow(), 2); // Getting the id of the selected row (ID of the movie)
                    clearActionListener(removeMovieButton);
                    removeMovieButton.addActionListener(e1 -> {  // Adding the selected movie to the watched list by id
                        Movie movieToDelete = moviesByID.searchMovieByID((Integer) id);
                        customer.getWatchedList().deleteMovie(movieToDelete);
                        JOptionPane.showMessageDialog(panel, movieToDelete.getTitle() + " deleted from watched list.");
                        saveData();
                        table.clearSelection();
                        accessHaveWatched(customer);
                    });

                    clearActionListener(addToWishlistButton);
                    addToWishlistButton.addActionListener(e2 -> {  // Adding the selected movie to the wishlist by id
                        Movie movieToAdd = moviesByID.searchMovieByID((Integer) id);
                        if (customer.getWishlist().addMovie(movieToAdd)) {
                            // If the wishlist is not full and successfully added the movie,
                            JOptionPane.showMessageDialog(panel, movieToAdd.getTitle() + " added to wishlist");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Can't add " + movieToAdd.getTitle() + " to the wishlist.\nWishlist is full!");
                        }
                        saveData();
                        table.clearSelection();
                        accessHaveWatched(customer);
                    });
                } else {
                    addToWishlistButton.setVisible(false);  // Don't show the option buttons if nothing is selected
                    removeMovieButton.setVisible(false);
                }
            }
        });

        // Refreshing the window
        panel.revalidate();
        panel.repaint();
    }

    private void viewByReleaseDateCustomer(Customer customer) {
        // Function to print the movies by release date on the screen
        panel.removeAll();  // Clearing the screen
        frame.setTitle(customer.getName() + " - View Movies by Release Date");

        // Adding the title label
        JLabel titleLabel = new JLabel("Movies by Release Date", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create a table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Movie Name");
        tableModel.addColumn("Release Date");
        tableModel.addColumn("ID");
        tableModel.addColumn("Score");
        tableModel.addColumn("Availability");

        getAscendingDateCustomer(moviesByDate, tableModel);  // Special in-order traversal to retrieve nodes by date and saving them into the tableModel

        // Creating the table
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {  // Being sure that the cells are not editable
                return false;
            }
        };
        table.setFont(new Font("Verdana", Font.PLAIN, 15));
        table.setRowHeight(25);

        // Setting column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(50);

        // Adding a scrollPane to the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300)); // Set a fixed size
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(panel.getBackground());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> customerMenu(customer));
        buttonPanel.add(backButton);

        // Adding buttons for wishlist and watched
        JButton addToWishlistButton = new JButton("Add to Wishlist");
        addToWishlistButton.setFont(buttonFont);
        addToWishlistButton.setVisible(false);
        buttonPanel.add(addToWishlistButton);

        JButton addToWatchedButton = new JButton("Add to Watched");
        addToWatchedButton.setFont(buttonFont);
        addToWatchedButton.setVisible(false);
        buttonPanel.add(addToWatchedButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        ListSelectionModel selectionModel = table.getSelectionModel();  // When a row is selected
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Clear all action listeners from the buttons
                for (ActionListener listener : addToWatchedButton.getActionListeners()) {
                    addToWatchedButton.removeActionListener(listener);
                }
                for (ActionListener listener : addToWishlistButton.getActionListeners()) {
                    addToWishlistButton.removeActionListener(listener);
                }

                if (table.getSelectedRow() != -1) {
                    addToWishlistButton.setVisible(true);  // Show the option buttons
                    addToWatchedButton.setVisible(true);

                    Object id = table.getValueAt(table.getSelectedRow(), 2); // Getting the id of the selected row (ID of the movie)

                    clearActionListener(addToWatchedButton);
                    addToWatchedButton.addActionListener(e1 -> {  // Adding the selected movie to the watched list by id

                        Movie movieToAdd = moviesByID.searchMovieByID((Integer) id);
                        if (customer.getWatchedList().searchMovieByID(movieToAdd.getID()) == null) {
                            customer.getWatchedList().insertMovie(movieToAdd);
                            JOptionPane.showMessageDialog(panel, movieToAdd.getTitle() + " added to the Have Watched.");
                        } else {
                            JOptionPane.showMessageDialog(panel, movieToAdd.getTitle() + " is already in the have watched list");
                        }
                        table.clearSelection();
                        saveData();
                        viewByReleaseDateCustomer(customer);
                    });

                    clearActionListener(addToWishlistButton);
                    addToWishlistButton.addActionListener(e2 -> {  // Adding the selected movie to the wishlist by id
                        Movie movieToAdd = moviesByID.searchMovieByID((Integer) id);
                        // If the add wishlist button is clicked, add movie to the wishlist and update the wishlist
                        if (customer.getWishlist().addMovie(movieToAdd)) {
                            // If the wishlist is not full and successfully added the movie,
                            JOptionPane.showMessageDialog(panel, movieToAdd.getTitle() + " added to wishlist");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Can't add " + movieToAdd.getTitle() + " to the wishlist.\nWishlist is full!");
                        }
                        saveData();
                        table.clearSelection();
                        viewByReleaseDateCustomer(customer);
                    });
                } else {
                    addToWishlistButton.setVisible(false);  // Don't show the option buttons if nothing is selected
                    addToWatchedButton.setVisible(false);
                }
            }
        });

        // Refreshing the window
        panel.revalidate();
        panel.repaint();
    }

    private void searchInHaveWatched(Customer customer) {
        panel.removeAll();  // Clearing the screen
        frame.setTitle(customer.getName() + " - Search in Have Watched");

        // Title label
        JLabel titleLabel = new JLabel("Search Movies By ID or Release Date", JLabel.CENTER);
        titleLabel.setFont(boldSubTitleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Search Panel for saving the input field, button and text fields
        JPanel searchPanel = new JPanel(new GridLayout(7, 2, 20, 0));
        searchPanel.setBackground(panel.getBackground());

        // Text-field for entering the movieID
        JTextField movieIdField = new JTextField(10);
        movieIdField.setFont(textFont);

        JTextField movieDateField = new JTextField(10);
        movieDateField.setFont(textFont);

        // Setting up the search button and movie labels
        JButton searchButtonID = new JButton("Search Movie by ID");
        JButton searchButtonDate = new JButton("Search Movie by Date");
        JLabel movieTitleLabel = new JLabel("Title: ");
        JLabel movieReleaseDateLabel = new JLabel("Release Date: ");
        JLabel movieIDLabel = new JLabel("ID: ");
        JLabel movieRatingLabel = new JLabel("Rating: ");
        JLabel movieAvailabilityLabel = new JLabel("Availability: ");
        JLabel empty = new JLabel("");
        JButton addWishlistButton = new JButton("Add Movie to the Wishlist");
        JButton removeFromHaveWatched = new JButton("Remove from Have Watched");
        searchButtonID.setFont(boldTextFont);  // Setting the font
        searchButtonDate.setFont(boldTextFont);
        movieTitleLabel.setFont(boldTextFont);
        movieReleaseDateLabel.setFont(boldTextFont);
        movieIDLabel.setFont(boldTextFont);
        movieRatingLabel.setFont(boldTextFont);
        movieAvailabilityLabel.setFont(boldTextFont);
        addWishlistButton.setFont(buttonFont);
        removeFromHaveWatched.setFont(buttonFont);
        addWishlistButton.setPreferredSize(new Dimension(250, 40)); // Setting the size of the buttons
        removeFromHaveWatched.setPreferredSize(new Dimension(250, 40));

        // Adding the button and the text fields to the searchPanel
        searchPanel.add(movieIdField);
        searchPanel.add(searchButtonID);
        searchPanel.add(movieDateField);
        searchPanel.add(searchButtonDate);
        searchPanel.add(movieTitleLabel);
        searchPanel.add(movieReleaseDateLabel);
        searchPanel.add(movieIDLabel);
        searchPanel.add(movieRatingLabel);
        searchPanel.add(movieAvailabilityLabel);
        searchPanel.add(empty);
        panel.add(searchPanel, BorderLayout.CENTER);  // Adding the search panel to the main panel

        // Creating a back button for going back to the customer menu
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> accessHaveWatched(customer));

        clearActionListener(searchButtonID);
        searchButtonID.addActionListener(e -> {
            // Search button that looks for a movie by id
            String movieId = movieIdField.getText();
            movieDateField.setText("");
            movieIdField.setText("");
            if (intValidation(movieId, 10000, 99999)) {  // Checking if the input is a valid integer in the range
                int movieIDInt = Integer.parseInt(movieId);
                Movie foundMovie = customer.getWatchedList().searchMovieByID(movieIDInt);
                if (foundMovie != null) {  // If the movie is found, update the text fields and show the details
                    movieTitleLabel.setText("Title: " + foundMovie.getTitle());
                    movieReleaseDateLabel.setText("Release Date: " + foundMovie.convertToDate());
                    movieRatingLabel.setText("Rating: " + foundMovie.getScore());
                    movieIDLabel.setText("ID: " + foundMovie.getID());
                    movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));
                    searchPanel.add(addWishlistButton);  // Also show the buttons as options
                    searchPanel.add(removeFromHaveWatched);

                    clearActionListener(addWishlistButton);
                    addWishlistButton.addActionListener(e12 -> {
                        // If the add wishlist button is clicked, add movie to the wishlist and update the wishlist
                        if (customer.getWishlist().addMovie(foundMovie)) {
                            // If the wishlist is not full and successfully added the movie,
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " added to wishlist");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Can't add " + foundMovie.getTitle() + " to the wishlist.\nWishlist is full!");
                        }
                        saveData();
                        searchInHaveWatched(customer);
                    });

                    clearActionListener(removeFromHaveWatched);
                    removeFromHaveWatched.addActionListener(e1 -> {
                        // If the have watched button is clicked, add the movie and update the have watched
                        customer.getWatchedList().deleteMovie(foundMovie);
                        saveData();
                        JOptionPane.showMessageDialog(panel, "Movie removed from haveWatched");
                        searchInHaveWatched(customer);
                    });
                } else {
                    // If the movie is not found
                    JOptionPane.showMessageDialog(panel, "No Movie Found with the specific movie ID");
                }
            } else {
                // If the input is invalid
                JOptionPane.showMessageDialog(panel, "Please enter a valid movie ID (10000 - 99999)");
            }
        });

        clearActionListener(searchButtonDate);
        searchButtonDate.addActionListener(e -> {
            // Search button that looks for a movie by id
            String movieDate = movieDateField.getText();
            movieDateField.setText("");
            movieIdField.setText("");

            if (intValidation(movieDate, 10000101, 99999999)) {  // Checking if the input is a valid integer in the range
                int movieDateInt = Integer.parseInt(movieDate);
                Movie foundMovie = customer.getWatchedList().searchMovieByDate(movieDateInt);
                if (foundMovie != null) {  // If the movie is found, update the text fields and show the details
                    movieTitleLabel.setText("Title: " + foundMovie.getTitle());
                    movieReleaseDateLabel.setText("Release Date: " + foundMovie.convertToDate());
                    movieRatingLabel.setText("Rating: " + foundMovie.getScore());
                    movieIDLabel.setText("ID: " + foundMovie.getID());
                    movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));
                    searchPanel.add(addWishlistButton);  // Also show the buttons as options
                    searchPanel.add(removeFromHaveWatched);

                    clearActionListener(addWishlistButton);
                    addWishlistButton.addActionListener(e12 -> {
                        // If the add wishlist button is clicked, add movie to the wishlist and update the wishlist
                        if (customer.getWishlist().addMovie(foundMovie)) {
                            // If the wishlist is not full and successfully added the movie,
                            JOptionPane.showMessageDialog(panel, foundMovie.getTitle() + " added to wishlist");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Can't add " + foundMovie.getTitle() + " to the wishlist.\nWishlist is full!");
                        }
                        saveData();
                        searchInHaveWatched(customer);
                    });

                    clearActionListener(removeFromHaveWatched);
                    removeFromHaveWatched.addActionListener(e1 -> {
                        // If the remove from have watched button is clicked, remove the movie
                        customer.getWatchedList().deleteMovie(foundMovie);
                        saveData();
                        JOptionPane.showMessageDialog(panel, "Movie removed from Watched List");
                    });
                } else {
                    // If the movie is not found
                    JOptionPane.showMessageDialog(panel, "No Movie Found with the specific release Date");
                }
            } else {
                // If the input is invalid
                JOptionPane.showMessageDialog(panel, "Please enter a valid Release Date (YYYYMMDD)");
            }
        });


        panel.add(backButton, BorderLayout.SOUTH);

        // Updating the screen
        panel.revalidate();
        panel.repaint();

    }

    private void editCustomerInformation(Customer customer) {
        // Function to create a new customer in our program
        panel.removeAll();  // Clear the panel
        frame.setTitle(customer.getName() + " - Edit Customer Information");

        // Main title of the page
        JLabel loginTitleLabel = new JLabel("Edit Customer Information", JLabel.CENTER);
        loginTitleLabel.setFont(titleFont);
        panel.add(loginTitleLabel, BorderLayout.NORTH);


        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        loginPanel.setBackground(panel.getBackground());  // Getting the background color of the main panel

        // Creating labels and text-fields for our program
        JLabel nameLabel = new JLabel("Name Surname", JLabel.RIGHT);
        JLabel emailLabel = new JLabel("Email Address", JLabel.RIGHT);
        JLabel passwordLabel = new JLabel("Password", JLabel.RIGHT);
        JTextField nameField = new JTextField();
        nameField.setText(customer.getName());
        JTextField emailField = new JTextField();
        emailField.setText(customer.getEmail());
        JPasswordField passwordField = new JPasswordField();
        passwordField.setText(customer.getPassword());
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Go Back");
        nameLabel.setFont(boldTextFont);
        nameField.setFont(textFont);
        emailLabel.setFont(boldTextFont);
        emailField.setFont(textFont);
        passwordLabel.setFont(boldTextFont);
        passwordField.setFont(textFont);
        submitButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        Dimension buttonSize = new Dimension(150, 50);  // Setting button size
        submitButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        panel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);  // Setting spacing

        JPanel buttonPanel = new JPanel(new FlowLayout());  // Button panel for storing the buttons
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createHorizontalStrut(60));
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Adding the fileds on the panel and adding the panel to the screen
        loginPanel.add(nameLabel);
        loginPanel.add(nameField);
        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(backButton);
        loginPanel.add(submitButton);
        panel.add(loginPanel, BorderLayout.CENTER);

        clearActionListener(submitButton);
        submitButton.addActionListener(e -> {
            // When submit button is clicked...
            // Getting the text in the fields
            String username = nameField.getText();
            String email = emailField.getText();
            String credit = String.valueOf(customer.getCredit());
            String password = new String(passwordField.getPassword());

            // Validating the input of the customer
            boolean valid = newCustomerValidation(username, email, credit, password);
            if (valid) {  // If it's valid, creates a new customer, shows a message and moves to the login menu
                customer.setName(username);
                customer.setEmail(email);
                customer.setPassword(password);
                saveData();
                JOptionPane.showMessageDialog(panel, "Customer information is changed");
                customerMenu(customer);
            } else {  // if it's not valid, shows another message
                JOptionPane.showMessageDialog(panel, "Invalid credentials.\nCheck that you entered a valid email and your credit card is a 4 digit integer.\nAlso be sure that you filled all of the fields.");
            }
        });

        // If the back button is clicked, removes everything and returns to the welcome menu.
        backButton.addActionListener(e -> customerMenu(customer));

        // Update the panel
        panel.revalidate();
        panel.repaint();
    }

    private static void getAscendingDateCustomer(MoviesByDate DateBST, DefaultTableModel tableModel) {
        // Special in-order traversal to print the movies by date and adding them to a table
        ascendCustomer(DateBST.getRoot(), tableModel);
        System.out.println();
    }

    private static void ascendCustomer(Movie currentMovie, DefaultTableModel tableModel) {
        // Recursive ascend function which will append the elements of the movie into an object and append it to the table
        if (currentMovie != null) {
            ascendCustomer(currentMovie.getRightDateMovie(), tableModel);
            if (currentMovie.getAvailability()) {
                Object[] rowData = {currentMovie.getTitle(), currentMovie.convertToDate(), currentMovie.getID(), currentMovie.getScore(), getAvailabilityText(currentMovie.getAvailability())};
                tableModel.addRow(rowData);
            }
            ascendCustomer(currentMovie.getLeftDateMovie(), tableModel);
        }
    }

    private static void getCustomerHaveWatched(HaveWatched haveWatched, DefaultTableModel tableModel) {
        Movie currentMovie = haveWatched.getHead();
        while (currentMovie != null) {
            Object[] rowData = {currentMovie.getTitle(), currentMovie.convertToDate(), currentMovie.getID(), currentMovie.getScore(), getAvailabilityText(currentMovie.getAvailability())};
            tableModel.addRow(rowData);
            currentMovie = currentMovie.getNextMovie();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////// ADMIN SIDE OF THE PROGRAM ////////////////////////////////////////////////////////////////////

    private void adminMenu() {
        // Creating the admin menu
        panel.removeAll();  // Clearing the screen
        frame.setTitle("Admin Menu");

        // Creating the main title
        JLabel adminTitleLabel = new JLabel("Admin Menu", JLabel.CENTER);
        adminTitleLabel.setFont(titleFont);
        panel.add(adminTitleLabel, BorderLayout.NORTH);

        // Create buttons for admin options
        JButton addMovieButton = new JButton("Add a new Movie");
        JButton leastRatedMovieButton = new JButton("View the least rated movie");
        JButton accessMoviesByIDorDate = new JButton("Access Movies by ID or Date");
        JButton moviesByDateButton = new JButton("View the movies in order of release date");
        JButton returnToSeedButton = new JButton("Return to the Initial Seed of the Program");
        JButton goMainMenu = new JButton("Logout to the main menu");

        addMovieButton.setFont(buttonFont);
        leastRatedMovieButton.setFont(buttonFont);
        accessMoviesByIDorDate.setFont(buttonFont);
        moviesByDateButton.setFont(buttonFont);
        returnToSeedButton.setFont(buttonFont);
        goMainMenu.setFont(buttonFont);

        // Add buttons to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.setLayout(new GridLayout(6, 1, 0, 15));
        buttonPanel.add(addMovieButton);
        buttonPanel.add(leastRatedMovieButton);
        buttonPanel.add(moviesByDateButton);
        buttonPanel.add(accessMoviesByIDorDate);
        buttonPanel.add(returnToSeedButton);
        buttonPanel.add(goMainMenu);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Calls the function that handles adding a new movie to the system
        addMovieButton.addActionListener(e -> addNewMovie());

        // Calls the function that removes the leastRatedMovie from the system
        leastRatedMovieButton.addActionListener(e -> removeLeastRated());

        // Calls the function that will print the movies by release date
        moviesByDateButton.addActionListener(e -> viewByReleaseDateAdmin());

        accessMoviesByIDorDate.addActionListener(e -> accessMoviesByIDorReleaseDateAdmin());

        returnToSeedButton.addActionListener(e -> returnToSeed());

        // Calls the function that will return to the main admin menu
        goMainMenu.addActionListener(e -> loginMenu("Admin"));

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void addNewMovie() {
        // Function that creates the interface for creating new movies
        panel.removeAll();  // Clearing the scree
        frame.setTitle("Add a New Movie");

        // Creating the title
        JLabel loginTitleLabel = new JLabel("New Movie Creation", JLabel.CENTER);
        loginTitleLabel.setFont(titleFont); // Setting font and size
        panel.add(loginTitleLabel, BorderLayout.NORTH); // Add the login title label to the top of the panel

        // Creating login panel for storing the login elements
        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 10, 5));
        loginPanel.setBackground(panel.getBackground());  // Setting background to the parent's background

        // Setting up the input labels, text-fields and buttons
        JLabel movieNameLabel = new JLabel("Movie Name", JLabel.RIGHT);
        JLabel releaseDateLabel = new JLabel("Release Date", JLabel.RIGHT);
        JLabel ratingLabel = new JLabel("Rating", JLabel.RIGHT);
        JLabel availableLabel = new JLabel("Available (0/1)", JLabel.RIGHT);
        JTextField movieNameField = new JTextField();
        JTextField releaseDateField = new JTextField();
        JTextField ratingField = new JTextField();
        JTextField avaliableField = new JTextField();
        JButton goBack = new JButton("Go Back");
        JButton submit = new JButton("Submit");
        movieNameLabel.setFont(subTitleFont);  // Setting the fonts
        movieNameField.setFont(subTitleFont);
        releaseDateLabel.setFont(subTitleFont);
        releaseDateField.setFont(subTitleFont);
        ratingLabel.setFont(subTitleFont);
        ratingField.setFont(subTitleFont);
        availableLabel.setFont(subTitleFont);
        avaliableField.setFont(subTitleFont);
        goBack.setFont(buttonFont);
        submit.setFont(buttonFont);
        Dimension buttonSize = new Dimension(200, 50);  // Setting button sizes
        goBack.setPreferredSize(buttonSize);
        submit.setPreferredSize(buttonSize);

        panel.add(Box.createVerticalStrut(20), BorderLayout.CENTER); // Adding spacing

        // Adding buttons to a panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(goBack);
        buttonPanel.add(Box.createHorizontalStrut(60));
        buttonPanel.add(submit);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        clearActionListener(submit);
        submit.addActionListener(e -> {
            // If the submit button is clicked...
            String movieName = movieNameField.getText();  // Retrieve the data in the text fields
            String releaseDate = releaseDateField.getText();
            String rating = ratingField.getText();
            String available = avaliableField.getText();
            if (newMovieValidation(movieName, releaseDate, rating, available)) {  // Perform validation
                int releaseDateNumeric = Integer.parseInt(releaseDateField.getText());
                int idNumeric = moviesByDate.getCount() + 1;
                int ratingNumeric = Integer.parseInt(ratingField.getText());
                boolean availableBoolean = Integer.parseInt(avaliableField.getText()) == 1;
                Movie newMovie = new Movie(movieName, releaseDateNumeric, idNumeric, ratingNumeric, availableBoolean);  // If valid, create a new movie
                moviesByID.insertMovieByID(newMovie);
                moviesByDate.insertMovieByDate(newMovie);
                movieScoresHeap.insertMovie(newMovie);
                saveData();
                JOptionPane.showMessageDialog(panel, movieName + " added successfully");  // Print a message to the screen
                adminMenu();  // Return to the admin menu
            } else {  // If validation is failed, print a message to the screen and list the rules.
                JOptionPane.showMessageDialog(panel, "Invalid Input!\n" + "Please be sure that all of the fields are filled.\n" + "Release Date is in the valid range of (10000101-99999999)\n" + "ID is in the valid range of 10000-99999\n" + "Rating is in the valid range of 0-100\n" + "And availability is 0 or 1");
            }
        });

        goBack.addActionListener(e -> adminMenu());  // If the back button is clicked, return to the admin menu

        // Add the elements to the login panel
        loginPanel.add(movieNameLabel);
        loginPanel.add(movieNameField);
        loginPanel.add(releaseDateLabel);
        loginPanel.add(releaseDateField);
        loginPanel.add(ratingLabel);
        loginPanel.add(ratingField);
        loginPanel.add(availableLabel);
        loginPanel.add(avaliableField);
        loginPanel.add(goBack);
        loginPanel.add(submit);

        // Adding the login panel to the screen
        panel.add(loginPanel, BorderLayout.CENTER);

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void removeLeastRated() {
        // Function to remove the least rated movie from the system
        panel.removeAll();  // Clearing the screen
        frame.setTitle("Least Rated Movie in the System");

        // Setting up the screen title
        JLabel leastRatedTitleLabel = new JLabel("Least Rated Movie", JLabel.CENTER);
        leastRatedTitleLabel.setFont(titleFont);
        panel.add(leastRatedTitleLabel, BorderLayout.NORTH);

        // Saving the leastRated movie in a variable
        Movie leastRated = movieScoresHeap.findMinScore();
        if (leastRated != null) {  // If the least rated movie is not null (not empty)
            JPanel movieInfoPanel = new JPanel(new GridLayout(5, 1));  // Create a new panel for displaying the information of the movie
            movieInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            movieInfoPanel.setBackground(panel.getBackground());

            // Creating fields for the attributes of the movie
            JLabel titleLabel = new JLabel("Title", JLabel.RIGHT);
            JLabel titleText = new JLabel(leastRated.getTitle(), JLabel.CENTER);
            JLabel releaseDateLabel = new JLabel("Release Date", JLabel.RIGHT);
            JLabel releaseDateText = new JLabel(leastRated.convertToDate(), JLabel.CENTER);
            JLabel idLabel = new JLabel("ID", JLabel.RIGHT);
            JLabel idText = new JLabel("" + leastRated.getID(), JLabel.CENTER);
            JLabel ratingLabel = new JLabel("Rating", JLabel.RIGHT);
            JLabel ratingText = new JLabel("" + leastRated.getScore(), JLabel.CENTER);
            JLabel availabilityLabel = new JLabel("Availability", JLabel.RIGHT);
            JLabel availabilityText = new JLabel(getAvailabilityText(leastRated.getAvailability()), JLabel.CENTER);
            titleLabel.setFont(boldSubTitleFont);  // Setting the font
            titleText.setFont(textFont);
            releaseDateLabel.setFont(boldSubTitleFont);
            releaseDateText.setFont(textFont);
            idLabel.setFont(boldSubTitleFont);
            idText.setFont(textFont);
            ratingLabel.setFont(boldSubTitleFont);
            ratingText.setFont(textFont);
            availabilityLabel.setFont(boldSubTitleFont);
            availabilityText.setFont(textFont);
            movieInfoPanel.add(titleLabel);  // Adding the elements to the panel
            movieInfoPanel.add(titleText);
            movieInfoPanel.add(releaseDateLabel);
            movieInfoPanel.add(releaseDateText);
            movieInfoPanel.add(idLabel);
            movieInfoPanel.add(idText);
            movieInfoPanel.add(ratingLabel);
            movieInfoPanel.add(ratingText);
            movieInfoPanel.add(availabilityLabel);
            movieInfoPanel.add(availabilityText);
            panel.add(movieInfoPanel, BorderLayout.CENTER);  // Adding the infoPanel to the main panel

            // Adding the buttons
            JButton goBackButton = new JButton("Go Back");
            JButton removeButton = new JButton("Remove the least Rated Movie");
            goBackButton.setFont(buttonFont);
            removeButton.setFont(buttonFont);

            // Action listener for goBackButton
            clearActionListener(removeButton);
            removeButton.addActionListener(e -> {
                // If the user choose to remove the least rated movie

                System.out.println("Heap");
                System.out.println(leastRated);
                System.out.println("Movies by ID");
                System.out.println(moviesByID.searchMovieByID(leastRated.getID()));
                System.out.println("Movies by Date");
                System.out.println(moviesByDate.searchMovieByDate(leastRated.getReleaseDate()));

                leastRated.setAvailability(false);  // Set the availability to false
                movieScoresHeap.deleteMinScore();
                moviesByID.deleteMovieID(leastRated);
                moviesByDate.deleteMovieDate(leastRated);
                saveData();
                Customer customer = customers.lookUpCustomer(1000);
                Wishlist wlist = customer.getWishlist();
                System.out.println(wlist.getFirstMovie());


                JOptionPane.showMessageDialog(panel, "Least Rated Movie has been removed successfully.");
                removeLeastRated();  // Return to the screen again to update
            });

            // If back-button is clicked, return to the adminMenu
            goBackButton.addActionListener(e -> adminMenu());

            // Adding the buttons to the screen
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center align the buttons
            buttonPanel.add(goBackButton);
            buttonPanel.add(Box.createHorizontalStrut(50));
            buttonPanel.add(removeButton);
            buttonPanel.setBackground(panel.getBackground());
            panel.add(buttonPanel, BorderLayout.SOUTH);
        } else {
            // If there are no movies in the system, prints a message
            JLabel noMoviesLabel = new JLabel("No available movies to show", JLabel.CENTER);
            noMoviesLabel.setFont(titleFont);
            panel.add(noMoviesLabel, BorderLayout.CENTER);

            // Adding a back button that'd return to the adminMenu
            JButton backButton = new JButton("Go Back");
            backButton.setFont(textFont);
            backButton.addActionListener(e -> adminMenu());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(backButton);
            buttonPanel.setBackground(panel.getBackground());
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void viewByReleaseDateAdmin() {
        // Function to print the movies by release date on the screen
        panel.removeAll();  // Clearing the screen
        frame.setTitle("Admin - View Movies by Release Date");

        // Adding the title label
        JLabel titleLabel = new JLabel("Movies by Release Date", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create a table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Movie Name");
        tableModel.addColumn("Release Date");
        tableModel.addColumn("ID");
        tableModel.addColumn("Score");
        tableModel.addColumn("Availability");

        getAscendingDateAdmin(moviesByDate, tableModel);  // Special in-order traversal to retrieve nodes by date and saving them into the tableModel

        // Creating the table
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {  // Being sure that the cells are not editable
                return false;
            }
        };
        table.setFont(new Font("Verdana", Font.PLAIN, 15));
        table.setRowHeight(25);

        // Setting column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(50);

        // Adding a scrollPane to the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300)); // Set a fixed size
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(panel.getBackground());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> adminMenu());
        buttonPanel.add(backButton);

        // Adding buttons for wishlist and watched
        JButton setAsAvaliableButton = new JButton("Set As Avaliable");
        setAsAvaliableButton.setFont(buttonFont);
        setAsAvaliableButton.setVisible(false);
        buttonPanel.add(setAsAvaliableButton);

        JButton setAsUnavaliableButton = new JButton("Set as Unavaliable");
        setAsUnavaliableButton.setFont(buttonFont);
        setAsUnavaliableButton.setVisible(false);
        buttonPanel.add(setAsUnavaliableButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        ListSelectionModel selectionModel = table.getSelectionModel();  // When a row is selected

        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Clear all action listeners from the buttons
                for (ActionListener listener : setAsAvaliableButton.getActionListeners()) {
                    setAsAvaliableButton.removeActionListener(listener);
                }
                for (ActionListener listener : setAsUnavaliableButton.getActionListeners()) {
                    setAsUnavaliableButton.removeActionListener(listener);
                }

                if (table.getSelectedRow() != -1) {
                    Object id = table.getValueAt(table.getSelectedRow(), 2); // Getting the id of the selected row (ID of the movie)
                    Movie movieToModify = moviesByID.searchMovieByID((Integer) id);

                    if (movieToModify.getAvailability()) {
                        setAsUnavaliableButton.setVisible(true);
                        setAsAvaliableButton.setVisible(false);
                        clearActionListener(setAsUnavaliableButton);
                        setAsUnavaliableButton.addActionListener(e2 -> {
                            movieToModify.setAvailability(false);
                            saveData();
                            table.clearSelection();
                            JOptionPane.showMessageDialog(panel, movieToModify.getTitle() + " Set to unavailable");
                            viewByReleaseDateAdmin();
                        });
                    } else {
                        setAsAvaliableButton.setVisible(true);
                        setAsUnavaliableButton.setVisible(false);
                        clearActionListener(setAsAvaliableButton);
                        setAsAvaliableButton.addActionListener(e2 -> {
                            movieToModify.setAvailability(true);
                            saveData();
                            table.clearSelection();
                            JOptionPane.showMessageDialog(panel, movieToModify.getTitle() + " Set to available");
                            viewByReleaseDateAdmin();
                        });
                    }
                } else {
                    setAsAvaliableButton.setVisible(false);  // Don't show the option buttons if nothing is selected
                    setAsUnavaliableButton.setVisible(false);
                }
            }
        });

        // Refreshing the window
        panel.revalidate();
        panel.repaint();
    }

    private void accessMoviesByIDorReleaseDateAdmin() {
        panel.removeAll();  // Clearing the screen
        frame.setTitle("Admin - Access Movies By ID or Release Date");

        // Title label
        JLabel titleLabel = new JLabel("Access Movies By ID or Release Date", JLabel.CENTER);
        titleLabel.setFont(boldSubTitleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Search Panel for saving the input field, button and text fields
        JPanel searchPanel = new JPanel(new GridLayout(7, 2, 20, 0));
        searchPanel.setBackground(panel.getBackground());

        // Text-field for entering the movieID
        JTextField movieIdField = new JTextField(10);
        movieIdField.setFont(textFont);

        JTextField movieDateField = new JTextField(10);
        movieDateField.setFont(textFont);

        // Setting up the search button and movie labels
        JButton searchButtonID = new JButton("Search Movie by ID");
        JButton searchButtonDate = new JButton("Search Movie by Date");
        JLabel movieTitleLabel = new JLabel("Title: ");
        JLabel movieReleaseDateLabel = new JLabel("Release Date: ");
        JLabel movieIDLabel = new JLabel("ID: ");
        JLabel movieRatingLabel = new JLabel("Rating: ");
        JLabel movieAvailabilityLabel = new JLabel("Availability: ");
        JLabel empty = new JLabel("");

        searchButtonID.setFont(boldTextFont);  // Setting the font
        searchButtonDate.setFont(boldTextFont);
        movieTitleLabel.setFont(boldTextFont);
        movieReleaseDateLabel.setFont(boldTextFont);
        movieIDLabel.setFont(boldTextFont);
        movieRatingLabel.setFont(boldTextFont);
        movieAvailabilityLabel.setFont(boldTextFont);

        // Adding the button and the text fields to the searchPanel
        searchPanel.add(movieIdField);
        searchPanel.add(searchButtonID);
        searchPanel.add(movieDateField);
        searchPanel.add(searchButtonDate);
        searchPanel.add(movieTitleLabel);
        searchPanel.add(movieReleaseDateLabel);
        searchPanel.add(movieIDLabel);
        searchPanel.add(movieRatingLabel);
        searchPanel.add(movieAvailabilityLabel);
        searchPanel.add(empty);
        panel.add(searchPanel, BorderLayout.CENTER);  // Adding the search panel to the main panel

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(panel.getBackground());


        // Creating a back button for going back to the customer menu
        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.addActionListener(e -> adminMenu());
        buttonPanel.add(backButton);

        // Creating a back button for going back to the customer menu
        JButton setAsUnavailableButton = new JButton("Set as Unavailable");
        setAsUnavailableButton.setFont(buttonFont);
        setAsUnavailableButton.setVisible(false);
        buttonPanel.add(setAsUnavailableButton);

        clearActionListener(searchButtonID);
        searchButtonID.addActionListener(e -> {
            // Search button that looks for a movie by id
            String movieId = movieIdField.getText();
            movieDateField.setText("");
            movieIdField.setText("");
            if (intValidation(movieId, 10000, 99999)) {  // Checking if the input is a valid integer in the range
                int movieIDInt = Integer.parseInt(movieId);
                Movie foundMovie = moviesByID.searchMovieByID(movieIDInt);  // If so, looking for the movie
                if (foundMovie != null) {  // If the movie is found, update the text fields and show the details
                    movieTitleLabel.setText("Title: " + foundMovie.getTitle());
                    movieReleaseDateLabel.setText("Release Date: " + foundMovie.convertToDate());
                    movieRatingLabel.setText("Rating: " + foundMovie.getScore());
                    movieIDLabel.setText("ID: " + foundMovie.getID());
                    movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));

                    if (foundMovie.getAvailability()) {
                        setAsUnavailableButton.setVisible(true);
                    }

                    clearActionListener(setAsUnavailableButton);
                    setAsUnavailableButton.addActionListener(e1 -> {
                        foundMovie.setAvailability(false);
                        movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));
                        saveData();
                        JOptionPane.showMessageDialog(panel, "Movie Set to unavailable");
                        accessMoviesByIDorReleaseDateAdmin();
                    });
                } else {
                    // If the movie is not found
                    JOptionPane.showMessageDialog(panel, "No Movie Found with the specific movie ID");
                }
            } else {
                // If the input is invalid
                JOptionPane.showMessageDialog(panel, "Please enter a valid movie ID (10000 - 99999)");
            }
        });

        clearActionListener(searchButtonDate);
        searchButtonDate.addActionListener(e -> {
            // Search button that looks for a movie by id
            String movieDate = movieDateField.getText();
            movieDateField.setText("");
            movieIdField.setText("");

            if (intValidation(movieDate, 10000101, 99999999)) {  // Checking if the input is a valid integer in the range
                int movieIDInt = Integer.parseInt(movieDate);
                Movie foundMovie = moviesByDate.searchMovieByDate(movieIDInt);  // If so, looking for the movie
                if (foundMovie != null) {  // If the movie is found, update the text fields and show the details
                    movieTitleLabel.setText("Title: " + foundMovie.getTitle());
                    movieReleaseDateLabel.setText("Release Date: " + foundMovie.convertToDate());
                    movieRatingLabel.setText("Rating: " + foundMovie.getScore());
                    movieIDLabel.setText("ID: " + foundMovie.getID());
                    movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));

                    if (foundMovie.getAvailability()) {
                        setAsUnavailableButton.setVisible(true);
                    }

                    clearActionListener(setAsUnavailableButton);
                    setAsUnavailableButton.addActionListener(e1 -> {
                        foundMovie.setAvailability(false);
                        movieAvailabilityLabel.setText("Availability: " + getAvailabilityText(foundMovie.getAvailability()));
                        saveData();
                        JOptionPane.showMessageDialog(panel, "Movie Set to unavailable");
                        accessMoviesByIDorReleaseDateAdmin();
                    });

                } else {
                    // If the movie is not found
                    JOptionPane.showMessageDialog(panel, "No Movie Found with the specific release Date");
                }
            } else {
                // If the input is invalid
                JOptionPane.showMessageDialog(panel, "Please enter a valid Release Date (YYYYMMDD)");
            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void returnToSeed() {
        // Show confirmation dialog
        int dialogResult = JOptionPane.showConfirmDialog(panel, "Are you sure you want to reset the data to the seed? It will delete all changes.", "Confirm Reset", JOptionPane.YES_NO_OPTION);

        // Check user's response
        if (dialogResult == JOptionPane.YES_OPTION) {
            // User wanted to proceed
            clearAllData();
            loadData();
            ImportManager importManager = new ImportManager(moviesByID, moviesByDate, movieScoresHeap, customers);
            importManager.importAllData();
            saveData();
            JOptionPane.showMessageDialog(panel, "Data reset to seed successfully.", "Reset Done", JOptionPane.INFORMATION_MESSAGE);
            panel.removeAll();
            welcomeMenu();
        }
    }

    private static void getAscendingDateAdmin(MoviesByDate DateBST, DefaultTableModel tableModel) {
        // Special in-order traversal to print the movies by date and adding them to a table
        ascendAdmin(DateBST.getRoot(), tableModel);
        System.out.println();
    }

    private static void ascendAdmin(Movie currentMovie, DefaultTableModel tableModel) {
        // Recursive ascend function which will append the elements of the movie into an object and append it to the table
        if (currentMovie != null) {
            ascendAdmin(currentMovie.getRightDateMovie(), tableModel);
            Object[] rowData = {currentMovie.getTitle(), currentMovie.convertToDate(), currentMovie.getID(), currentMovie.getScore(), getAvailabilityText(currentMovie.getAvailability())};
            tableModel.addRow(rowData);
            ascendAdmin(currentMovie.getLeftDateMovie(), tableModel);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////// OTHER GUI COMPONENTS /////////////////////////////////////////////////////////////////////////

    private void welcomeMenu() {
        // Main menu of our program that has 3 options: Customer login - Admin Login and New Customer Creation
        // Title of the welcome menu
        JLabel titleLabel = new JLabel("Parker Films", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 60));
        panel.add(titleLabel, BorderLayout.CENTER); // Add the title label to the center of the panel

        // Creating buttons, setting fonts and sizes
        JButton customerLoginButton = new JButton("Customer");
        JButton adminLoginButton = new JButton("Admin");
        JButton newCustomerButton = new JButton("New Customer");
        JButton quitButton = new JButton("Quit");
        customerLoginButton.setFont(buttonFont);
        adminLoginButton.setFont(buttonFont);
        newCustomerButton.setFont(buttonFont);
        quitButton.setFont(buttonFont);
        Dimension buttonSize = new Dimension(200, 70);
        customerLoginButton.setPreferredSize(buttonSize);
        adminLoginButton.setPreferredSize(buttonSize);
        newCustomerButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);

        // Spacing between the elements
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panel.getBackground());  // Matching the background color
        buttonPanel.add(customerLoginButton);
        buttonPanel.add(Box.createHorizontalStrut(10));  // Adding space between buttons
        buttonPanel.add(adminLoginButton);
        buttonPanel.add(Box.createHorizontalStrut(10));  // Adding space between buttons
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(Box.createHorizontalStrut(10));  // Adding space between buttons
        buttonPanel.add(quitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);  // Adding the button panel to the bottom of the panel

        // If the customer login button is clicked, create a login menu in the customer version
        customerLoginButton.addActionListener(e -> loginMenu("Customer"));

        // If the admin login button is clicked, create a login menu in the admin version
        adminLoginButton.addActionListener(e -> loginMenu("Admin"));

        // If the new customer button is clicked, go to the sign-up page
        newCustomerButton.addActionListener(e -> newCustomerMenu());

        quitButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            frame.dispose();

            System.exit(0);
        });


        // Updating the screen
        panel.revalidate();
        panel.repaint();
    }

    private void loginMenu(String userType) {
        // The login menu that can be used both for admin and the customer
        panel.removeAll();  // Clear the panel
        frame.setTitle(userType + " Login");

        // Creating the page title using the input string
        JLabel loginTitleLabel = new JLabel(userType + " Login", JLabel.CENTER);
        loginTitleLabel.setFont(titleFont);
        panel.add(loginTitleLabel, BorderLayout.NORTH);

        JPanel loginPanel = new JPanel(new GridLayout(4, 1, 0, 50));  // Creating a panel for storing the login elements
        loginPanel.setBackground(panel.getBackground());  // Setting the background color to the parent

        // Creating labels and buttons
        JLabel usernameLabel = new JLabel("Username", JLabel.CENTER);
        JLabel passwordLabel = new JLabel("Password", JLabel.CENTER);
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Go Back");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        usernameLabel.setFont(subTitleFont);
        usernameField.setFont(subTitleFont);
        usernameField.setPreferredSize(new Dimension(150, 20));
        passwordLabel.setFont(subTitleFont);
        passwordField.setFont(subTitleFont);
        loginButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        Dimension buttonSize = new Dimension(200, 40);  // Setting button size
        loginButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        panel.add(Box.createVerticalStrut(5), BorderLayout.CENTER);  // Adding spacing

        // Creating another panel for the buttons and adding buttons in it
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(panel.getBackground());  // Setting background color to the parent
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(120));  // Adding spacing
        buttonPanel.add(loginButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);  // Add buttons to the bottom of the panel

        JPanel emptyPanel = new JPanel(new FlowLayout());
        emptyPanel.setBackground(panel.getBackground());

        // Adding the login fields to the login panel
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        // Add login panel to the center of the main panel
        panel.add(loginPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        clearActionListener(loginButton);
        loginButton.addActionListener(e -> {
            // When the login button is clicked...
            String username = usernameField.getText();  // Retrieve the text in the fields
            String password = new String(passwordField.getPassword());
            if (userType.equals("Admin")) {  // If the user type is admin
                if (adminValidation(username, password)) {  // Perform the admin validation and if validated, move to the admin menu
                    JOptionPane.showMessageDialog(panel, "Admin successfully logged in");
                    adminMenu();
                } else {  // If validation was unsuccessfull show a message
                    JOptionPane.showMessageDialog(panel, "Admin username / password is wrong");
                }
            } else if (userType.equals("Customer")) {  // If the user type is customer
                if (customerValidation(username, password, customers)) {  // Perform customer validation and if validated
                    Customer currentCustomer = customers.lookUpCustomer(Integer.parseInt(username));  // Find the customer in the customerStorage
                    JOptionPane.showMessageDialog(panel, "Customer successfully logged in");
                    customerMenu(currentCustomer);  // Move to the customer menu with the customer object
                } else {  // If the customer couldn't be validated show a message
                    JOptionPane.showMessageDialog(panel, "Customer username / password is wrong.\n" + "Your username is the last 4 digits of your credit card.\n" + "Your password is 'password' by default or the password you set.\n" + "Be sure that you're registered in the system!");
                }
            }
        });

        backButton.addActionListener(e -> {
            // If the back button is clicked, return to the welcome menu
            panel.removeAll();
            welcomeMenu();
        });

        // Update the panel
        panel.revalidate();
        panel.repaint();
    }

    private void newCustomerMenu() {
        // Function to create a new customer in our program
        panel.removeAll();  // Clear the panel
        frame.setTitle("Create New Customer");

        // Main title of the page
        JLabel loginTitleLabel = new JLabel("New Customer Creation", JLabel.CENTER);
        loginTitleLabel.setFont(titleFont);
        panel.add(loginTitleLabel, BorderLayout.NORTH);


        JPanel loginPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        loginPanel.setBackground(panel.getBackground());  // Getting the background color of the main panel

        // Creating labels and text-fields for our program
        JLabel nameLabel = new JLabel("Name Surname", JLabel.RIGHT);
        JLabel emailLabel = new JLabel("Email Address", JLabel.RIGHT);
        JLabel creditLabel = new JLabel("Credit Card Number", JLabel.RIGHT);
        JLabel passwordLabel = new JLabel("Password", JLabel.RIGHT);
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField creditField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Go Back");
        nameLabel.setFont(boldTextFont);
        nameField.setFont(textFont);
        emailLabel.setFont(boldTextFont);
        emailField.setFont(textFont);
        creditLabel.setFont(boldTextFont);
        creditField.setFont(textFont);
        passwordLabel.setFont(boldTextFont);
        passwordField.setFont(textFont);
        submitButton.setFont(buttonFont);
        backButton.setFont(buttonFont);
        Dimension buttonSize = new Dimension(150, 50);  // Setting button size
        submitButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        panel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);  // Setting spacing

        JPanel buttonPanel = new JPanel(new FlowLayout());  // Button panel for storing the buttons
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createHorizontalStrut(60));
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Adding the fileds on the panel and adding the panel to the screen
        loginPanel.add(nameLabel);
        loginPanel.add(nameField);
        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(creditLabel);
        loginPanel.add(creditField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(backButton);
        loginPanel.add(submitButton);
        panel.add(loginPanel, BorderLayout.CENTER);

        clearActionListener(submitButton);
        submitButton.addActionListener(e -> {
            // When submit button is clicked...
            // Getting the text in the fields
            String username = nameField.getText();
            String email = emailField.getText();
            String credit = creditField.getText();
            String password = new String(passwordField.getPassword());

            // Validating the input of the customer
            boolean valid = newCustomerValidation(username, email, credit, password);
            if (valid) {  // If it's valid, creates a new customer, shows a message and moves to the login menu
                Customer customer = new Customer(username, email, Integer.parseInt(credit), new Wishlist(), password);
                customers.insertCustomer(customer);
                saveData();
                JOptionPane.showMessageDialog(panel, "Customer successfully created");
                loginMenu("Customer");
            } else {  // if it's not valid, shows another message
                JOptionPane.showMessageDialog(panel, "Invalid credentials.\nCheck that you entered a valid email and your credit card is a 4 digit integer.\nAlso be sure that you filled all of the fields.");
            }
        });

        backButton.addActionListener(e -> {
            // If the back button is clicked, removes everything and returns to the welcome menu.
            panel.removeAll();
            welcomeMenu();
        });

        // Update the panel
        panel.revalidate();
        panel.repaint();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////// EXTRA FUNCTIONS ////////////////////////////////////////////////////////////////////////////////

    private void setPanel(JPanel panel) {
        this.panel = panel;
    }

    private void setFrame(JFrame frame) {
        this.frame = frame;
    }

    private static boolean adminValidation(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    private static boolean customerValidation(String username, String password, CustomerStorage customers) {
        if (!intValidation(username, 1000, 9999)) {
            return false;
        } else {
            int usernameInt = Integer.parseInt(username);
            Customer foundCustomer = customers.lookUpCustomer(usernameInt);
            if (foundCustomer == null) {
                return false;
            } else {
                String customerPassword = foundCustomer.getPassword();
                return password.equals(customerPassword);
            }
        }
    }

    private static boolean newCustomerValidation(String username, String email, String credit, String password) {
        // Checking if the entered credentials are valid email / integer / not empty.
        return intValidation(credit, 1000, 9999) && emailValidation(email) && !username.isEmpty() && !password.isEmpty();
    }

    private static boolean intValidation(String textInt, int lower, int higher) {
        // Function that checks if an input is a valid integer in the valid range
        int intNumber;
        try {
            intNumber = Integer.parseInt(textInt);  // Trying to parse int
        } catch (NumberFormatException e) {  // If this fails, this means that it's not a valid integer
            return false;
        }
        return (intNumber >= lower) && (intNumber <= higher);  // Checkin for the range
    }

    private static boolean emailValidation(String email) {
        // Function that performs email validation by checking different rules:
        // 1. Email contains a valid ending
        // 2. Email contains @ (doesn't start or end with it)
        // 3. Both sides of the @ is full (preventing @yahoo.com or derin@.com)
        return email.contains("@") && !email.startsWith("@") && !email.endsWith("@") && !email.contains("@.") && (email.endsWith(".com") || email.endsWith(".edu") || email.endsWith(".gov") || email.endsWith(".org") || email.endsWith(".net") || email.endsWith(".info") || email.endsWith(".biz"));
    }

    private static boolean newMovieValidation(String movieName, String releaseDate, String rating, String available) {
        // Performing validation in the new movie creation process.
        // Checks if the name field is not empty and the other fields are integers in the valid range
        return !movieName.isEmpty() && intValidation(releaseDate, 10000101, 99999999) && intValidation(rating, 0, 100) && intValidation(available, 0, 1);
    }

    private static String getAvailabilityText(boolean availability) {
        return availability ? "Available" : "Unavailable";
    }

    private static void nodeChecker(MoviesByDate mbd, MoviesByID mbi, MovieScoresHeap msh) {
        // Function to check the location of the same Node in the different data structures
        // It's not used anywhere but it's helpful for debugging
        System.out.println("NODE CHECKER");
        System.out.println("CURRENTLY LEAST RATED NODE");
        Movie leastRated = msh.findMinScore();

        if (leastRated != null) {
            System.out.println(leastRated.getTitle());
            System.out.println(leastRated);
            System.out.println(leastRated.getTitle());
            System.out.println(mbd.searchMovieByDate(leastRated.getReleaseDate()));
            System.out.println(leastRated.getTitle());
            System.out.println(mbi.searchMovieByID(leastRated.getID()));
        }
    }

    private static void clearActionListener(JButton button) {
        // Function checks if there are previous action listeners in the button and cleans them
        ActionListener[] listeners = button.getActionListeners();
        if (listeners.length > 0) {
            button.removeActionListener(listeners[0]);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////// SAVE - LOAD FUNCTIONS //////////////////////////////////////////////////////////////////////////

    private void saveData() {
        try {
            System.out.println("Saving data into the structures...");
            FileOutputStream file = new FileOutputStream("data/programData.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.moviesByID);
            out.writeObject(this.moviesByDate);
            out.writeObject(this.movieScoresHeap);
            out.writeObject(this.wishlist);
            out.writeObject(this.haveWatched);
            out.writeObject(this.customers);
            out.close();
            file.close();
            System.out.println("Data is saved to the different structures");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            System.out.println("Loading Movie data from data/programData.ser...");
            File file = new File("data/programData.ser");
            boolean fileCreated = file.createNewFile();
            FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileInput);
            this.moviesByID = (MoviesByID) in.readObject();
            this.moviesByDate = (MoviesByDate) in.readObject();
            this.movieScoresHeap = (MovieScoresHeap) in.readObject();
            this.wishlist = (Wishlist) in.readObject();
            this.haveWatched = (HaveWatched) in.readObject();
            this.customers = (CustomerStorage) in.readObject();
            fileInput.close();
            in.close();
            System.out.println("Data is successfully loaded from the data/programData.ser.");
        } catch (FileNotFoundException e) {
            System.out.println("data/programData.ser file is not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        } catch (EOFException e) {
            System.out.println("data/programData.ser is Empty");
            this.moviesByID = new MoviesByID();
            this.moviesByDate = new MoviesByDate();
            this.movieScoresHeap = new MovieScoresHeap();
            this.customers = new CustomerStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAllData() {
        try {
            System.out.println("Clearing all data...");
            emptyFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("All data has been cleared");
    }

    private void emptyFile() throws IOException {
        File file = new File("data/programData.ser");
        FileWriter fwOb = new FileWriter(file, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        ParkerFilmsGUI filmManager = new ParkerFilmsGUI();

        // Creating the window
        JFrame frame = new JFrame("Parker Films");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        filmManager.setFrame(frame);

        // Create a panel to store the application
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(VERY_LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        filmManager.setPanel(panel);

        filmManager.welcomeMenu();

        // Showing the window
        frame.add(panel);
        frame.setVisible(true);
    }
}
