
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ANNAPOORNA_21114_assembler 
{
	public static File no_whitespace() throws IOException 
	{
		File file1 = new File("C:\\Users\\ANNAPOORNA\\Desktop\\ANNAPOORNA AK_AM.EN.U4AIE21114\\nand2tetris\\projects\\06\\max\\Max.asm");
		File newfile1 = new File("_no_whitespace.asm");
		Scanner in = new Scanner(file1);
		FileWriter fw = new FileWriter(newfile1);

		String ans = "";
		String line = "";
		
        //skipping empty-lines
		while (in.hasNextLine())
		{
			line = in.nextLine();
			if (line != "") 
			{
				ans = ans + line + "\n";
			}
		}

		fw.write(ans);
		fw.close();
		return newfile1;
	}
	
    //removing comments
	public static File removecomment(File file) throws IOException 
	{
		Scanner in = new Scanner(new FileInputStream(file));
		File commentlessFile = new File("commentless_file.asm");
		FileWriter fw = new FileWriter(commentlessFile);
		String line;
		while (in.hasNext()) 
		{
			line = in.nextLine();
			if (!String.valueOf(line.charAt(0)).equals("/")) //ignoring comments
			{
				String parts[] = line.split("/"); //for in-line comments
				fw.write(parts[0] + "\n");
			}
		}
		fw.close();
		in.close();
		return (commentlessFile);
	}

	public static File final_instruction(File file) throws IOException
	{
		HashMap<String, Integer> s_t = new HashMap<>();
		HashMap<String, String> dest = new HashMap<>();
		HashMap<String, String> jmp = new HashMap<>();
		HashMap<String, String> comp_1 = new HashMap<>();
		HashMap<String, String> comp_2 = new HashMap<>();

		//symbol table
		s_t.put("SP", 0);
		s_t.put("LCL", 1);
		s_t.put("ARG", 2);
		s_t.put("THIS", 3);
		s_t.put("THAT", 4);
		s_t.put("SCREEN", 16384);
		s_t.put("KBD", 24567);
		s_t.put("R0", 0);
		s_t.put("R1", 1);
		s_t.put("R2", 2);
		s_t.put("R3", 3);
		s_t.put("R4", 4);
		s_t.put("R5", 5);
		s_t.put("R6", 6);
		s_t.put("R7", 7);
		s_t.put("R8", 8);
		s_t.put("R9", 9);
		s_t.put("R10", 10);
		s_t.put("R11", 11);
		s_t.put("R12", 12);
		s_t.put("R13", 13);
		s_t.put("R14", 14);
		s_t.put("R15", 15);

		// values in dest
		dest.put("null", "000");
		dest.put("M", "001");
		dest.put("D", "010");
		dest.put("MD", "011");
		dest.put("A", "100");
		dest.put("AM", "101");
		dest.put("AD", "110");
		dest.put("AMD", "111");

		// values in jmp
		jmp.put("null", "000");
		jmp.put("JGT", "001");
		jmp.put("JEQ", "010");
		jmp.put("JGE", "011");
		jmp.put("JLT", "100");
		jmp.put("JNE", "101");
		jmp.put("JLE", "110");
		jmp.put("JMP", "111");

		// values in comp1
		comp_1.put("0", "101010");
		comp_1.put("1", "111111");
		comp_1.put("-1", "111010");
		comp_1.put("D", "001100");
		comp_1.put("A", "110000");
		comp_1.put("!D", "001111");
		comp_1.put("!A", "110001");
		comp_1.put("-D", "001111");
		comp_1.put("D+1", "011111");
		comp_1.put("A+1", "110111");
		comp_1.put("D-1", "001110");
		comp_1.put("A-1", "110010");
		comp_1.put("D+A", "000010");
		comp_1.put("D-A", "010011");
		comp_1.put("A-D", "000111");
		comp_1.put("D&A", "000000");
		comp_1.put("D|A", "010101");

		// values in comp2
		comp_2.put("M", "110000");
		comp_2.put("!M", "110001");
		comp_2.put("-M", "110011");
		comp_2.put("M+1", "110111");
		comp_2.put("M-1", "110010");
		comp_2.put("D+M", "000010");
		comp_2.put("D-M", "010011");
		comp_2.put("M-D", "000111");
		comp_2.put("D&M", "000000");
		comp_2.put("D|M", "010101");

		Scanner in = new Scanner(new FileInputStream(file));
		Scanner in_l = new Scanner(new FileInputStream(file));
		File binaryfile = new File("final_Add_withsymbol.hack");
		FileWriter fw = new FileWriter(binaryfile);

		String line;
		char firstChar;
		String instruction = "";
		String tempStr = "";
		String jump = "null";
		String a;
		int n = 16;
		int l = 0;//no. lines in the code
		int l_n = 0;// no. of labels added

		while (in_l.hasNext()) 
		{
			String labelline = in_l.nextLine().trim();
			char firstChar_l = labelline.charAt(0);
			if (Character.toString(firstChar_l).equals("("))
			{
				String label = labelline.substring(1);
				label = label.replace(")", "");
				s_t.put(label, l-l_n);//adding it to the symbol table
				l_n++;
			}
			l++;
		}

		while (in.hasNext()) 
		{
			line = (in.nextLine()).trim();
			firstChar = line.charAt(0);
			String s1 = Character.toString(firstChar);
			String s2 = "@";
			//all instructions starting with @
			if (Character.toString(firstChar).equals("@")) 
			{
				instruction = line.substring(1);
				if (s_t.containsKey(instruction))
				{
					//if its present in the symbol table
					int p = s_t.get(instruction);
					String binaryP = Integer.toBinaryString(p);
					if (binaryP.length() < 16)
					{
						int zn = 16 - binaryP.length();
						String zeroes = "0".repeat(zn);
						String lastStep = zeroes + binaryP;
						Long.parseLong(lastStep);
						fw.write(lastStep);
						fw.write("\r\n");
					}
				}
				else
				{
					//if its a number
					if (instruction.matches("[0-9]+"))
					{
						int p = Integer.parseInt(instruction);
						String binaryP = Integer.toBinaryString(p);
						if (binaryP.length() < 16) 
						{
							int zn = 16 - binaryP.length();
							String zeroes = "0".repeat(zn);
							String lastStep = zeroes + binaryP;
							Long.parseLong(lastStep);
							fw.write(lastStep);
							fw.write("\r\n");
						}
					}
					else 
					{
						//if it's any variable add it to the symbol table starting from 16
						if (instruction != "") 
						{
							tempStr = "";
							tempStr = tempStr + Integer.toBinaryString(n);
							s_t.put(instruction, n);//adding in to the st
							n++;
							if (tempStr.length() < 16)
							{
								int zn = 16 - tempStr.length();
								String zeroes = "0".repeat(zn);
								String lastStep = zeroes + tempStr;
								fw.write(lastStep);
								fw.write("\r\n");
							}
						}
					}
				}
			} 
			//C-instruction
			else if (s1 != s2)
			{
				instruction = line;
				for (int i = 0; i < instruction.length(); i++)
				{
					if (String.valueOf(instruction.charAt(i)).equals("="))
					{
						String[] tempStr1 = instruction.split("=");
						String x = tempStr1[0];
						String y = tempStr1[1];
						String ones = "111";
						String done = "";
						if (dest.containsKey(x))
						{
							if (comp_1.containsKey(y)) 
							{
								a = "0";
								done = ones + a + comp_1.get(y) + dest.get(x) + jmp.get(jump);
								fw.write(done);
								fw.write("\r\n");
							} 
							else
							{
								a = "1";
								done = ones + a + comp_2.get(y) + dest.get(x) + jmp.get(jump);
								fw.write(done);
								fw.write("\r\n");
							}
						}
					} 
					else if (String.valueOf(instruction.charAt(i)).equals(";"))
					{
						String[] tempStr1 = instruction.split(";");
						String x = tempStr1[0];
						String y = tempStr1[1];
						String ones = "111";
						String done = "";
						if (comp_1.containsKey(x)) 
						{
							if (jmp.containsKey(y))
							{
								a = "0";
								done = ones + a + comp_1.get(x) + dest.get(jump) + jmp.get(y);
								fw.write(done);
								fw.write("\r\n");
							}

						} 
						else 
						{
							if (jmp.containsKey(y)) 
							{
								a = "1";
								done = ones + a + comp_2.get(x) + dest.get(jump) + jmp.get(y);
								fw.write(done);
								fw.write("\r\n");
							}
						}
					}
				}
			}
		}
		fw.close();
		return (binaryfile);
	}

	public static void main(String[] args) throws IOException 
	{
		File file1 = no_whitespace();
		File file2 = removecomment(file1);
		final_instruction(file2);
	}
}
