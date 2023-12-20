using System.Numerics;

class Utils
{
    public static UInt128 Gcf(UInt128 n1, UInt128 n2)
    {
        UInt128 a, b;

        if (n1 > n2)
        {
            a = n1; b = n2;
        }
        else
        {
            a = n2; b = n1;
        }

        while (b != 0)
        {
            UInt128 r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static UInt128 Lcm(UInt128 a, UInt128 b)
    {
        return (a * b) / Gcf(a, b);
    }

    public static UInt128 LcmEucl(IEnumerable<UInt128> numbers)
    {
        if (numbers.Count() < 2)
        {
            throw new Exception("numbers must have at least 2 entries");
        }

        return numbers.Aggregate((UInt128)0, (acc, n) =>
        {
            return (acc > 0) ? Lcm(acc, n) : n;
        });
    }

    public static (uint, uint) Lcm(IEnumerable<uint> numbers)
    {
        if (numbers.Count() < 2)
        {
            throw new Exception("numbers must have at least 2 entries");
        }

        uint lmin = numbers.Min();
        uint ln;

        for (ln = 1; !numbers.All((n) => ((lmin * ln) % n) == 0); ln++)
        {
        }

        return (lmin, ln);
    }
}