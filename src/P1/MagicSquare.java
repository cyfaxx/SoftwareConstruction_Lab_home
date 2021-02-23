package P1;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.*;

public class MagicSquare
{
    private static final int MAX_SIZE = 1000;

    public static void main(String[] args)
    {
        System.out.println("有两种模式：T,P,分别表示判断是否幻方和生成幻方");

        Scanner scan;

        while (true)
        {
            System.out.println("请输入模式和文件名：(或输入over以结束)");

            scan = new Scanner(System.in);
            String str = scan.nextLine();

            if (str.equals("over"))
            {
                scan.close();
                return;
            } else if (str.charAt(0) == 'T')
            {
                System.out.println(isLegalMagicSquare(str.substring(2)));
            } else if (str.charAt(0) == 'P')
            {
                System.out.println("请输入参数n");
                Scanner scan_2 = new Scanner(System.in);
                int n;

                try
                {
                    n = Integer.parseInt(scan_2.next());
                } catch (NumberFormatException e5)
                {
                    System.err.println("n需要为整数");
                    continue;
                }

                try
                {
                    generateMagicSquare(n, str.substring(2));
                }
                catch (IOException e6)
                {
                    System.err.println("出问题了,但我不知道是什么问题，没学");
                }
            }


        }
    }

    public static boolean isLegalMagicSquare(String Filename)
    {
        String file_path = "./src/P1" + Filename;

        FileReader f;
        try
        {
            f = new FileReader(file_path, StandardCharsets.UTF_8);
        } catch (IOException e1)
        {
            System.err.println("找不到此文件");
            return false;
        }

        char[] data_char_array = new char[MAX_SIZE * MAX_SIZE];

        try
        {
            f.read(data_char_array);
        } catch (IOException e2)
        {
            System.err.println("读写文件失败");
            return false;
        }

        int count = 0;
        while (data_char_array[count++] != '\u0000') ;

        char[] data_char_array_2 = new char[count - 1];
        System.arraycopy(data_char_array, 0, data_char_array_2, 0, count - 1);

        String data_str = String.valueOf(data_char_array_2);
        String[] lines_str = data_str.split("\n|\r");

        int line_num = lines_str.length;
        int[][] lines_int = new int[line_num][line_num];
        String[] line_str;

        for (int i = 0; i < line_num; i++)
        {
            line_str = lines_str[i].split("\t");

            int j = 0;

            for (String word : line_str)
            {
                try
                {
                    lines_int[i][j++] = Integer.parseInt(word);
                } catch (NumberFormatException e3)
                {
                    try
                    {
                        Double.parseDouble(word);
                        System.err.println("第" + (i + 1) + "行" + "第" + j + "列非正整数");
                    } catch (NumberFormatException e4)
                    {
                        System.err.println("第" + (i + 1) + "行" + "第" + j + "列出现非/t分隔符");
                    }
                    return false;
                }
            }

            if (j != line_num)
            {
                System.err.println("第" + (i + 1) + "行只有" + j + "个数,应有" + line_num + "个数");
                return false;
            }
        }

        int total_standard = 0;
        int total_diagonal_0 = 0;
        int total_diagonal_1 = 0;

        for (int i = 0; i < line_num; i++)
        {
            total_standard += lines_int[0][i];
            total_diagonal_0 += lines_int[i][i];
            total_diagonal_1 += lines_int[i][line_num - 1];
        }

        if (total_diagonal_0 != total_standard || total_diagonal_1 != total_standard)
        {
            System.err.println("对角线之和与第一行和不同");
            return false;
        }

        for (int i = 0; i < line_num; i++)
        {
            int total_line = 0;
            int total_row = 0;

            for (int j = 0; j < line_num; j++)
            {
                total_line += lines_int[i][j];
                total_row += lines_int[j][i];
            }

            if (total_line != total_standard)
            {
                System.err.println("第" + (i + 1) + "行与第一行和不同");
                return false;
            }

            if (total_row != total_standard)
            {
                System.err.println("第" + (i + 1) + "列与第一行和不同");
                return false;
            }
        }

        return true;
    }

    public static boolean generateMagicSquare(int n, String Filename) throws IOException
    {
        if (n < 0)
        {
            System.err.println("n不能为负数");
            return false;
        }

        if (n % 2 == 0)
        {
            System.err.println("n不能为偶数");
            return false;
        }

        String file_path = "./src/P1" + Filename;


        BufferedWriter writer = new BufferedWriter(new FileWriter(file_path));

        int[][] magic = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;

        for (i = 1; i <= square; i++)
        {
            magic[row][col] = i;
            if (i % n == 0)
                row++;
            else
            {
                if (row == 0)
                    row = n - 1;
                else
                    row--;
                if (col == (n - 1))
                    col = 0;
                else
                    col++;
            }
        }

        try
        {
            for (i = 0; i < n; i++)
            {
                for (j = 0; j < n; j++)
                    writer.write(magic[i][j] + "\t");
                writer.write("\n");
            }

            writer.flush();
            writer.close();
        }
        catch (IOException e6)
        {
            System.err.println("写入文件失败");
            return false;
        }

        return true;
    }

}