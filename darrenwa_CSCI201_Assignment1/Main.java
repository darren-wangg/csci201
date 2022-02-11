import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Main {
	private ArrayList<TimefallShelter> shelters = new ArrayList<>();;
	private WristCuff cuff;
	static Scanner in = new Scanner(System.in);
	private String file_name;

	/**
	 * Uses GSON to read the file inputed by the user
	 */
	private void readFile() {
		System.out.print("Please provide timefall shelter data source: ");
		this.file_name = in.nextLine();

		// empty line, prompt again
		if (this.file_name.length() == 0) {
			this.readFile();
			return;
		}

		try {
			// process JSON file
			Gson gson = new Gson();

			// convert JSON string to User object
			TimefallShelter[] shelters_json = gson.fromJson(new FileReader(this.file_name), TimefallShelter[].class);

			// validate input file has correct data types
			/*
			 * chiralFrequency: int
			 * timefall: bool
			 * guid: String
			 * name: String
			 * phone: String
			 * address: String
			 */
			// add all shelters to instance var
			for (TimefallShelter shelter : shelters_json) {
				// at least one missing key
				if (shelter.getFrequency() == -1) {
					throw new NoSuchFieldException("chiralFrequency");
				}
				if (shelter.getGuid() == null) {
					throw new NoSuchFieldException("GUID");
				}
				if (shelter.getName() == null) {
					throw new NoSuchFieldException("name");
				}
				if (shelter.getPhone() == null) {
					throw new NoSuchFieldException("phone");
				}
				if (shelter.getAddy() == null) {
					throw new NoSuchFieldException("address");
				}

				// get rid of whitespace
				shelter.setGuid(shelter.getGuid().trim());
				shelter.setName(shelter.getName().trim());
				shelter.setPhone(shelter.getPhone().trim());
				shelter.setAddy(shelter.getAddy().trim());

				// check if keys have correct data types

				// validate phone number format (use regex)
				if (!shelter.getPhone().matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")) {
					throw new NumberFormatException("Phone Number");
				}

				// validate GUID (globally unique identifier)
				if (!shelter.getGuid().matches("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$")) {
					throw new NumberFormatException("GUID");
				}

				// then, add
				this.shelters.add(shelter);
			}
		}
		// potential errors
		catch (FileNotFoundException error) {
			System.err.println("The file " + this.file_name + " could not be found.");
			this.readFile();
			return;
		} catch (NumberFormatException error) {
			System.err.println("Data parameters cannot be converted to the proper type: " + error.getMessage());
			this.readFile();
			return;
		} catch (NoSuchFieldException error) {
			System.err.println("Missing data parameters: " + error.getMessage());
			this.readFile();
			return;
		} catch (JsonParseException error) {
			System.err.println("Json invalid");
			this.readFile();
			return;
		}

		System.out.println("=== Data accepted ===");
	}

	/**
	 * Gets the supported chiral frequencies from the user
	 */
	private void setSupportedFrequencies() {
		// get user input
		System.out.print("Please provide supported Chiral frequencies: ");
		String input = in.nextLine();

		// populate frequencies arraylist
		ArrayList<Integer> frequencies = new ArrayList<Integer>();
		String[] input_split = input.split(", ");
		for (String str : input_split) {
			// validate user input
			try {
				frequencies.add(Integer.parseInt(str));
			} catch (Exception error) {
				continue;
			}
		}

		// initialize WristCuff instance
		cuff = new WristCuff(frequencies, this.shelters, this.file_name);
	}

	/**
	 * Prints the option menu
	 */
	private void displayOptions() {
		System.out.println(
				"\n1) List all available shelters within the min and max of supported Chiral frequencies\n"
						+ "2) Search for a shelter by Chiral frequency\n"
						+ "3) Search for a shelter by name\n"
						+ "4) Sort shelters by Chiral frequency\n"
						+ "5) Jump to a shelter with the lowest supported Chiral frequency");

		System.out.print("Choose an option: ");
		String option_input = in.nextLine();

		// empty line, prompt again
		if (option_input.length() == 0) {
			System.out.println("Please enter a valid integer from 1 - 5.");
			this.displayOptions();
			return;
		}

		System.out.println();
		int option;
		try {
			option = Integer.parseInt(option_input);
		} catch (NumberFormatException error) {
			System.out.println("Please enter a valid integer from 1 - 5.");
			this.displayOptions();
			return;
		}

		while (true) {
			boolean foundOption = false;

			// process options
			switch (option) {
				case 1:
					// print shelters within range of supported frequencies
					cuff.listAllShelters();
					foundOption = true;
					break;
				case 2:
					// search for specific shelter (by frequency)
					while (true) {
						System.out.print("What shelter frequency are you looking for? ");
						int freq = in.nextInt();
						// eat newline
						in.nextLine();
						TimefallShelter search_freq = cuff.searchShelter(freq);

						// not found, try again
						if (search_freq == null) {
							System.out.println("\nThat Chiral frequency does not exist.\n");
						}
						// found, print it out
						else {
							System.out.println("\nFound!\n");
							System.out.println(search_freq);
							foundOption = true;
							break;
						}
					}
					break;
				case 3:
					// search for specific shelter (by name)
					while (true) {
						System.out.print("What shelter's name are you looking for? ");
						String name = in.nextLine();
						TimefallShelter search_name = cuff.searchShelter(name);

						// not found, try again
						if (search_name == null) {
							System.out.println("\nNo such shelter...\n");
						}
						// found, print it out
						else {
							System.out.println("\nFound!\n");
							System.out.println(search_name);
							foundOption = true;
							break;
						}
					}
					break;
				case 4:
					// sort shelters
					cuff.sortShelters();

					System.out.println("Shelters successfully sorted by Chiral frequency.");
					foundOption = true;
					break;
				case 5:
					// jump to lowest supported freq shelter
					System.out.println("=== Commencing timefall shelter search ===");
					TimefallShelter found_shelter = cuff.findShelter();

					if (found_shelter != null) {
						foundOption = true;
						System.out.println("=== Matching timefall shelter found! ===");
						System.out.println(found_shelter);

						System.out.println("=== Commencing Chiral jump, see you in safety. ===");
						System.exit(0);
					} else {
						System.out.println("=== No shelter available. You are DOOMED. ===");
						System.exit(0);
					}
					break;
			}

			// if not option 1 - 5
			if (!foundOption) {
				System.out.print("Invalid. Please enter another option: ");
				try {
					option = in.nextInt();
				} catch (InputMismatchException error) {
					in.nextLine();
					System.out.println("Please enter a valid integer from 1 - 5.");
					this.displayOptions();
					return;
				}
			} else {
				this.displayOptions();
			}
		}
	}

	public static void main(String[] args) {
		Main solution = new Main();
		System.out.println("Welcome to Bridges Link.\n");

		solution.readFile();
		solution.setSupportedFrequencies();

		solution.displayOptions();
	}
}
