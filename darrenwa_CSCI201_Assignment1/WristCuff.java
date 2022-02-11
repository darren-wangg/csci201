import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.*;

public class WristCuff {
    private ArrayList<Integer> supportedFrequencies;
    private ArrayList<TimefallShelter> shelters;
    private String file_name;

    // set initial values for needed members
    public WristCuff(ArrayList<Integer> supportedFrequencies, ArrayList<TimefallShelter> shelters, String file_name) {
        this.supportedFrequencies = supportedFrequencies;
        this.shelters = shelters;
        this.file_name = file_name;
    }

    /**
     * List all available shelters that have supported Chiral frequencies
     */
    void listAllShelters() {
        ArrayList<TimefallShelter> range_shelters = new ArrayList<>();

        for (TimefallShelter shelter : this.shelters) {
            int freq = shelter.getFrequency();
            // supported freq
            if (this.supportedFrequencies.contains(freq)) {
                // AND not experiencing timefall
                if (!shelter.hasTimefall()) {
                    range_shelters.add(shelter);
                }
            }
        }

        System.out.println(range_shelters.size() + " results");
        System.out.println();

        // print each shelter within the range
        for (TimefallShelter shelter : range_shelters) {
            // call TimefallShelter's toString()
            System.out.println(shelter);
        }
    }

    /**
     * Search for a shelter by Chiral frequency
     */
    public TimefallShelter searchShelter(int freq) {
        for (TimefallShelter shelter : this.shelters) {
            // found
            if (shelter.getFrequency() == freq) {
                return shelter;
            }
        }

        return null;
    }

    /**
     * Search for a shelter by name
     */
    public TimefallShelter searchShelter(String name) {
        for (TimefallShelter shelter : this.shelters) {
            // found
            if (shelter.getName().toUpperCase().equals(name.toUpperCase())) {
                return shelter;
            }
        }

        return null;
    }

    /**
     * Sort shelters by Chiral frequency
     */
    public void sortShelters() {
        Collections.sort(this.shelters);
    }

    /**
     * Find an available shelter with the lowest supported Chiral frequency
     */
    public TimefallShelter findShelter() {
        // first: sort all supported freq's
        Collections.sort(this.supportedFrequencies);

        for (int freq : this.supportedFrequencies) {
            TimefallShelter shelter = this.searchShelter(freq);
            // shelter does not exist, just go to next one
            if (shelter == null) {
                continue;
            }

            // prune unavailable shelters (where timefall == true)
            // is experiencing timefall, remove bc not a valid shelter
            if (shelter.hasTimefall()) {
                System.out.println(
                        "=== Chiral frequency " + shelter.getFrequency() + " unstable, Chiral jump unavailable. ===");
                System.out.println(
                        "=== Removing target shelter from the list of shelters and saving updated list to disk. ===\n");

                // remove from file
                this.shelters.remove(shelter);

                // read JSON file & parse
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try {
                    FileWriter file = new FileWriter(this.file_name);
                    String jsonOutput = gson.toJson(this.shelters);
                    file.write(jsonOutput);
                    file.close();
                } catch (Exception error) {
                    System.out.println("File writer error: " + error);
                    System.exit(0);
                }

                // skip this shelter
                continue;
            }

            // supported
            if (this.supportedFrequencies.contains(freq)) {
                return shelter;
            }
        }

        // no shelters are supported, just return null
        return null;
    }

    /**
     * Saves the updated list of shelters to disk
     */
    public void save() throws FileNotFoundException {

    }
}
