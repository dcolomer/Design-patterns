//---------------------------------------------------------------------
// Author - Alex.C.P (alexcpn@gmail.com)
// This class models the ThreadPool
//---------------------------------------------------------------------
package evolution.threads.threadpool;

import java.lang.reflect.*;
import java.util.Comparator;

public class Command<T> {

    private Object[] argParameter;

    //Ctor for a method with zero args
    Command(T pObj, String mthodName, long timeout, String key) {
        m_objptr = pObj;
        m_methodName = mthodName;
        m_timeout = timeout;
        m_key = key;

    }

    //Ctor for a method with two args
    Command(T pObj, String mthodName, long timeout, String key, int arg1) {
        m_objptr = pObj;
        m_methodName = mthodName;
        m_timeout = timeout;
        m_key = key;

    }
    //Ctor for a method with two args

    Command(T pObj, String mthodName, long timeout, String key, int arg1, int arg2) {
        m_objptr = pObj;
        m_methodName = mthodName;
        m_timeout = timeout;
        m_key = key;
        argParameter = new Object[2];
        argParameter[0] = arg1;
        argParameter[1] = arg2;

    }

    // Calls the method of the object
    void execute() {

        //argParameter = new Object[2];
        //argParameter[0] = 1;
        //argParameter[1] = 2;

        Class klass = m_objptr.getClass();
        Class[] paramTypes = new Class[]{int.class, int.class};

        try {
            Method methodName = klass.getMethod(m_methodName, paramTypes);
            //System.out.println("Found the method--> " + methodName);
            if (argParameter.length == 2) {
                methodName.invoke(m_objptr, (Object) argParameter[0],
                        (Object) argParameter[1]);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // This is the timout value beyond which if this task's
    // excecute method takes it is deemed as hanging
    // NOTE - Timeout is in seconds
    long GetTimeOut() {
        return m_timeout;
    }

    // return a key associated with this command object
    int GetKey() {
        return m_key.hashCode();
        // return RSHash(m_key);
    }
    private T m_objptr;
    String m_methodName;
    long m_timeout;
    String m_key;
    long m_value;

    public int compare(Command<T> a) {
        //System.out.println("Comparing mine" + m_value + " " + a.m_value);
        if (m_value < a.m_value) {
            return 1;
        }
        if (m_value > a.m_value) {
            return -1;
        }
        if (m_value == a.m_value) {
            return 0;
        }
        return 0;
    }
    // /Function object for use with the priority queue to check the priority
    // /of the Command object
}
