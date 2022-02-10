import java.util.Arrays;

/*
 * Lab02.
 *
 *
*  Adding extra helper functions is allowed and would be a good idea.
 * Extra class variables can be added but are not required to solve this lab.
 *
 * It's always a good idea to split your logic into multiple functions since each
 * function should ideally do one thing and do it well.
 *
 * Good luck! Have fun! (If you're not having fun ask CPs for help :-)
*/

public class Uba<AnyType>  implements UbaInterface<AnyType>
{
    private static final int DEFAULT_CAPACITY = 5;

    private AnyType[] items;
    private int size; // the current number of elements in the array

  /**
	*  Constructs an empty array of default capacity
	*/
	@SuppressWarnings("unchecked")
	public Uba()
	{
		this.items = new <AnyType>[];
		this.size = DEFAULT_CAPACITY;
	}


  /**
	*  Adds an item to this collection, at the end.
	*/
	public void add(AnyType x)
	{
		// edge cases
		if (x == null) return;
		
		// check if need to expand
		if (this.items.length == this.size) {
			AnyType[] tmp = new <AnyType>[this.size * 2];
			
			// copy over old values
			System.arraycopy(tmp, 0, this.items, 0, this.size);
			this.items = tmp;
		}
		
		// otherwise, just add to array
		else {
			this.items[this.size] = x;
		}
		
		// increment size
		this.size++;
	}

  /**
	*   Removes the last item from the list and returns it
	*/
	public AnyType remove()
	{
		// edge cases
		if (this.items.length == 0) return;
		
		// remove last element
		int tmp = this.items[this.size - 1];
		Arrays.copyOf(this.items, this.size - 1);
		
		// decrement size
		this.size--;
		
		return tmp;
	}


  /**
	*  Returns a string representation
	*/
	@Override
	public String toString( )
	{
		// edge cases
		if (this.items.length == 0) return "[]";
		
		String output = "[";
		
		for (int i = 0; i < this.size; i++) {
			output += this.items[i] + ", ";
		}
		
		output += "]";
		
		return output;
	}

	public static void main(String[] args)
	{
		Uba<Integer> tmp = new Uba<Integer> ();
		System.out.println(tmp);

		for(int i = 0; i < 50; i++) tmp.add(i);
		System.out.println(tmp);

		System.out.println(tmp.remove());
		System.out.println(tmp);

		Uba< String > tmp1 = new Uba<String> ();
		for(int i = 0; i < 6; i++) tmp1.add("uba" + i);
		System.out.println(tmp1);

		System.out.println(tmp1.remove());
		System.out.println(tmp1);

	}

}