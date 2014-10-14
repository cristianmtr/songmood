import java.util.Scanner;
import java.text.*;

public class Matrix{

	private double myMatrix[][];

    //Takes y-dimension and x-dimension
	public Matrix(int yDimension, int xDimension){
		myMatrix = new double[yDimension][xDimension];
	}

    //Takes a string as input, with the dimensions at the start and values following
	public Matrix(String input){
		Scanner scan = new Scanner(input);

		int yDimension = scan.nextInt();
		int xDimension = scan.nextInt();
		
		myMatrix = new double[yDimension][xDimension];

		for(int y = 0; y < yDimension; y++){
			for(int x = 0; x < xDimension; x++){

				double test = Double.parseDouble(scan.next());
				myMatrix[y][x] = test;
			}
		}

		scan.close();
	}

    //Inserts value val at index (y, x)
    public void insert(int y, int x, double val){
        setValue(y, x, val);
        return;
    }

    //Sets value val at index (y, x)
	public void setValue(int y, int x, double val){
		if(x < 0 || x >= getX() || y < 0 || y >= getY()){
			System.err.println("Setting value matrix with wrong dimensions! " +  y + "/" + getY() + " "  + x + "/" + getX());
			//return;
		}
		myMatrix[y][x] = val; 
		return;
	}

    public double get(int y, int x){
        return getValue(y, x);
    }

    //Gets the value at index (y, x)
	public double getValue(int y, int x){
		if(x < 0 || x >= getX() || y < 0 || y >= getY()){
			System.err.println("Getting matrix with wrong dimensions! " +  y + "/" + getY() + " "  + x + "/" + getX());
			//return 0.0f;
		}

		return myMatrix[y][x];
	}


    //Gets the size of the rows
	public int getRowSize(){
		return myMatrix[0].length;
	}

    //Gets the size of the columns
	public int getColumnSize(){
		return myMatrix.length;
	}

    //Gets the row at index y
    public Matrix getRow(int y){

        Matrix m = new Matrix(1, this.getX());

        for(int x = 0; x < this.getX(); x++){
            m.setValue(0, x, this.getValue(y, x));
        }

        return m;
    }

    //Gets the column at index x
    public Matrix getColumn(int x){

        Matrix m = new Matrix(this.getY(), 1);

        for(int y = 0; y < this.getY(); y++){
            m.setValue(y, 0, this.getValue(y, x));
        }

        return m;
    }

    //Sets the row at index y to m
    public void setRow(int y, Matrix m){
        for(int x = 0; x < m.getX(); x++){
            this.setValue(y, x, m.getValue(0, x));
        }
    }

    //Sets the column at index x to m
    public void setColumn(int x, Matrix m){
        for(int y = 0; y < m.getY(); y++){
            this.setValue(y, x, m.getValue(y, 0));
        }
    }

    //Divides the element at index (y, x) with value div
    public void divide(int y, int x, double div){
        this.setValue(y, x, this.getValue(y, x) / div);
    }

    //Normalizes the matrix by row
    public void normalize(){

        double sum = 0.0;

        for(int y = 0; y < this.getY(); y++){

            sum = Matrix.sum(this.getRow(y));

            for(int x = 0; x < this.getX(); x++){
                this.setValue(y, x, this.getValue(y, x) / sum);
            }
        }
    }

    //Normalizes the row y with value div
    public void normalizeRow(int y, double div){

        for(int x = 0; x < this.getX(); x++){
            //System.err.println("before: " + this.getValue(y, x) + ", after: " + this.getValue(y, x) / norm);
            this.setValue(y, x, this.getValue(y, x) / div);
        }
    }

    //Multiples two matrices a and b
	public static Matrix multiply(Matrix a, Matrix b){
		if(a.getX() != b.getY()){
			System.err.println("Can't multiply with those dimensions! " + a.getX() + " " + b.getY());
			return null;
		}

		Matrix c = new Matrix(a.getY(), b.getX());

		double ans = 0;

		for(int y = 0; y < a.getY(); y++){
			for (int x = 0; x < b.getX(); x++ ) {

				ans = 0;

				for (int k = 0; k < a.getX(); k++) { // equals b.getY()
					ans += a.getValue(y, k) * b.getValue(k, x);
				}

				c.setValue(y, x, ans);
			}
		}

		return c;
	}

    //Returns the sum of all values in the matrix
    public static double sum(Matrix m){

        double sum = 0.0;

        for(int y = 0; y < m.getY(); y++){
            for(int x = 0; x < m.getX(); x++){
                sum += m.getValue(y, x);
            }
        }

        return sum;
    }

    //Returns all rows summed into one row, then normalized
    public static Matrix sumRows(Matrix m){

        Matrix sRows = new Matrix(1, m.getX());
        double sum = 0.0;

        for(int x = 0; x < m.getX(); x++){

            sum = 0.0;

            for(int y = 0; y < m.getY(); y++){
                sum += m.getValue(y, x);
            }

            sRows.setValue(0, x, sum);
        }

        sRows.normalize();

        return sRows;
    }

    //Returns a copy of the matrix
    public Matrix copy(){
        Matrix m = new Matrix(this.getY(), this.getX());

        for(int y = 0; y < this.getY(); y++){
            for(int x = 0; x < this.getX(); x++){
                m.setValue(y, x, this.getValue(y, x));
            }
        }

        return m;
    }

    //Compares whether the matrices m1 and m2 are equal
    public static boolean compare(Matrix m1, Matrix m2){

        double epsilon = 0.00001;

        if(m1.getX() != m2.getX() || m1.getY() != m2.getY()){
            System.err.println("Comparing matrices of different dimensions!");
            return false;
        }

        for(int y = 0; y < m1.getY(); y++){
            for(int x = 0; x < m1.getX(); x++){
                if(Math.abs(m1.getValue(y, x) - m2.getValue(y, x)) > epsilon || Double.isNaN(m1.getValue(y, x)) || Double.isNaN(m2.getValue(y, x))){
                    return false;
                }
            }
        }

        return true;
    }

    //Returns the matrix as a string
	public String toString(){

        DecimalFormat df = new DecimalFormat("0.0#######"); //df.format(
		String str = new String();

		for(int y = 0; y < getY(); y++){
			for(int x = 0; x < getX(); x++){
				str += getValue(y, x) + " ";
			}
		}

		return getY() + " " + getX() + " " + str.replace(',', '.') + "\n";
	}

    //Returns the matrix represented with rows and columns as a string
	public String toPrettyString(){

        DecimalFormat df = new DecimalFormat("0.00");
		String str = new String();

		for(int y = 0; y < getY(); y++){
			for(int x = 0; x < getX(); x++){
				str += df.format(getValue(y, x)) + "\t";
			}

			str += "\n";
		}

		return str.replace(',', '.');
	}

    //Fills the matrix with normalized values
    public void fill(){
        for(int y = 0; y < getY(); y++){
            for(int x = 0; x < getX(); x++){
                this.setValue(y, x, 1.0 / getX());
            }
        }
    }
}