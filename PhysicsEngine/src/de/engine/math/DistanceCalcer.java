package de.engine.math;

public final class DistanceCalcer
{
    public interface IFunction
    {
        public double function(double x);
    }
    
    
    public class StraightLine implements IFunction
    {
        private double m;
        private double n;
        
        private double a;
        private double b;
        
        
        public StraightLine(Vector p1, Vector p2)
        {
            m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
            n = p2.getY() - m * p2.getX();
            
            a = p1.getX();
            b = p2.getX();
        }
        
        
        public double function(double x)
        {
            return m * x + n;
        }
        
        
        public double getA()
        {
            return a;
        }
        
        public void setA(double a)
        {
            this.a = a;
        }
        
        
        public double getB()
        {
            return b;
        }
        
        public void setB(double b)
        {
            this.b = b;
        }
    }
    
    private static final double eps_sqrt = Math.sqrt(r8_epsilon());
    
    private double tolerance;
    private Vector point;
    private IFunction function;
    
    private double lastSolvedX;
    
    
    public DistanceCalcer() { }
    
    
    public DistanceCalcer(double tolerance)
    {
        this.tolerance = tolerance;
    }
    
    
    public DistanceCalcer(double tolerance, Vector point)
    {
        this.tolerance = tolerance;
        this.point = point;
    }
    
    
    public DistanceCalcer(double tolerance, IFunction function)
    {
        this.tolerance = tolerance;
        this.function = function;
    }
    
    
    public DistanceCalcer(double tolerance, IFunction function, Vector point)
    {
        this.tolerance = tolerance;
        this.function = function;
        this.point = point;
    }
    
    
    public double calculateDistanceBetweenFunctionPoint(double intervalStart, double intervalEnd)
    {
        IFunction distFunc = new IFunction()
        {
            @Override
            public double function(double x)
            {
                double v1 = point.getX() - x;
                double v2 = point.getY() - function.function(x);
                
                return Math.sqrt(v1 * v1 + v2 * v2);
            }
        };
        
        lastSolvedX = calculateMinimum(intervalStart, intervalEnd, tolerance, distFunc);
        return distFunc.function(lastSolvedX);
    }
    
    
    public double calculateMinimum(double intervalStart, double intervalEnd)
    {
        return lastSolvedX = calculateMinimum(intervalStart, intervalEnd, tolerance, function);
    }
    
    
    public static double calculateMinimum(double a, double b, double tolerance, IFunction f)
    
    // ****************************************************************************80
    //
    // Purpose:
    //
    // LOCAL_MIN seeks a local minimum of a function F(X) in an interval [A,B].
    //
    // Discussion:
    //
    // The method used is a combination of golden section search and
    // successive parabolic interpolation. Convergence is never much slower
    // than that for a Fibonacci search. If F has a continuous second
    // derivative which is positive at the minimum (which is not at A or
    // B), then convergence is superlinear, and usually of the order of
    // about 1.324....
    //
    // The values EPS and T define a tolerance TOL = EPS * abs ( X ) + T.
    // F is never evaluated at two points closer than TOL.
    //
    // If F is a unimodal function and the computed values of F are always
    // unimodal when separated by at least SQEPS * abs ( X ) + (T/3), then
    // LOCAL_MIN approximates the abscissa of the global minimum of F on the
    // interval [A,B] with an error less than 3*SQEPS*abs(LOCAL_MIN)+T.
    //
    // If F is not unimodal, then LOCAL_MIN may approximate a local, but
    // perhaps non-global, minimum to the same accuracy.
    //
    // Licensing:
    //
    // This code is distributed under the GNU LGPL license.
    //
    // Modified:
    //
    // 17 July 2011
    //
    // Author:
    //
    // Original FORTRAN77 version by Richard Brent.
    // C++ version by John Burkardt.
    // Modifications by John Denker.
    //
    // Reference:
    //
    // Richard Brent,
    // Algorithms for Minimization Without Derivatives,
    // Dover, 2002,
    // ISBN: 0-486-41998-3,
    // LC: QA402.5.B74.
    //
    // Parameters:
    //
    // Input, double A, B, the endpoints of the interval.
    //
    // Input, double T, a positive absolute error tolerance.
    //
    // Input, func_base& F, a user-supplied c++ functor whose
    // local minimum is being sought. The input and output
    // of F() are of type double.
    //
    // Output, double &X, the estimated value of an abscissa
    // for which F attains a local minimum value in [A,B].
    //
    // Output, double LOCAL_MIN, the value F(X).
    //
    {
        double x;
        double c;
        double d;
        double e;
//        double eps;
        double fu;
        double fv;
        double fw;
        double fx;
        double m;
        double p;
        double q;
        double r;
        double sa;
        double sb;
        double t2;
        double tol;
        double u;
        double v;
        double w;
        //
        // C is the square of the inverse of the golden ratio.
        //
        c = 0.5 * (3.0 - Math.sqrt(5.0));
        
//        eps = Math.sqrt(r8_epsilon());
        
        sa = a;
        sb = b;
        x = sa + c * (b - a);
        w = x;
        v = w;
        d = 0.0;
        e = 0.0;
        fx = f.function(x);
        fw = fx;
        fv = fw;
        
        
        for (;;)
        {
            m = 0.5 * (sa + sb);
            tol = eps_sqrt * Math.abs(x) + tolerance;
            t2 = 2.0 * tol;
            //
            // Check the stopping criterion.
            //
            if (Math.abs(x - m) <= t2 - 0.5 * (sb - sa))
            {
                break;
            }
            //
            // Fit a parabola.
            //
            r = 0.0;
            q = r;
            p = q;
            
            if (tol < Math.abs(e))
            {
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2.0 * (q - r);
                if (0.0 < q)
                {
                    p = -p;
                }
                q = Math.abs(q);
                r = e;
                e = d;
            }
            
            if (Math.abs(p) < Math.abs(0.5 * q * r) && q * (sa - x) < p && p < q * (sb - x))
            {
                //
                // Take the parabolic interpolation step.
                //
                d = p / q;
                u = x + d;
                //
                // F must not be evaluated too close to A or B.
                //
                if ((u - sa) < t2 || (sb - u) < t2)
                {
                    if (x < m)
                    {
                        d = tol;
                    }
                    else
                    {
                        d = -tol;
                    }
                }
            }
            //
            // A golden-section step.
            //
            else
            {
                if (x < m)
                {
                    e = sb - x;
                }
                else
                {
                    e = sa - x;
                }
                d = c * e;
            }
            //
            // F must not be evaluated too close to X.
            //
            if (tol <= Math.abs(d))
            {
                u = x + d;
            }
            else if (0.0 < d)
            {
                u = x + tol;
            }
            else
            {
                u = x - tol;
            }
            
            fu = f.function(u);
            //
            // Update A, B, V, W, and X.
            //
            if (fu <= fx)
            {
                if (u < x)
                {
                    sb = x;
                }
                else
                {
                    sa = x;
                }
                v = w;
                fv = fw;
                w = x;
                fw = fx;
                x = u;
                fx = fu;
            }
            else
            {
                if (u < x)
                {
                    sa = u;
                }
                else
                {
                    sb = u;
                }
                
                if (fu <= fw || w == x)
                {
                    v = w;
                    fv = fw;
                    w = u;
                    fw = fu;
                }
                else if (fu <= fv || v == x || v == w)
                {
                    v = u;
                    fv = fu;
                }
            }
        }
        return x;
    }
    
    static double r8_epsilon()
    
    // ****************************************************************************80
    //
    // Purpose:
    //
    // R8_EPSILON returns the R8 round off unit.
    //
    // Discussion:
    //
    // R8_EPSILON is a number R which is a power of 2 with the property that,
    // to the precision of the computer's arithmetic,
    // 1 < 1 + R
    // but
    // 1 = ( 1 + R / 2 )
    //
    // Licensing:
    //
    // This code is distributed under the GNU LGPL license.
    //
    // Modified:
    //
    // 08 May 2006
    //
    // Author:
    //
    // John Burkardt
    //
    // Parameters:
    //
    // Output, double R8_EPSILON, the double precision round-off unit.
    //
    {
        double r = 1.0;
        
        while (1.0 < (1.0 + r))
        {
            r /= 2.0;
        }
        
        return (2.0 * r);
    }
    
    
    public double getTolerance()
    {
        return tolerance;
    }
    
    public void setTolerance(double tolerance)
    {
        this.tolerance = tolerance;
    }
    
    
    public Vector getPoint()
    {
        return point;
    }
    
    public void setPoint(Vector point)
    {
        this.point = point;
    }
    
    
    public IFunction getFunction()
    {
        return function;
    }
    
    public void setFunction(IFunction function)
    {
        this.function = function;
    }
    
    
    public double getLastSolvedX()
    {
        return lastSolvedX;
    }
}