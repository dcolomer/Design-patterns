//---------------------------------------------------------------------
// Author - Alex.C.P (alexcpn@gmail.com)
// This class models the ThreadPool
//---------------------------------------------------------------------
package evolution.threads.threadpool;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

class CRequestQueue<T> {

    Compare<Command<T>> compare = new Compare<Command<T>>();
    private PriorityQueue<Command<T>> REQUESTQUEUE = new PriorityQueue<Command<T>>(10, (Comparator<Command<T>>) compare);
    //HashMap<Long, PriorityQueue<Command<T>>> RequestQueue;
    ConcurrentHashMap<Long, PriorityQueue<Command<T>>> RequestQueue;
    Iterator REQUESTQUEUEITR = REQUESTQUEUE.iterator();
    Iterator CurrentItr;
    // HashMap<Long,PriorityQueue<Command<T>>::Iterator<E> REQUESTQUEUEITR;
    // typedef typename map<long,REQUESTQUEUE >::iterator REQUESTQUEUEITR;
    // todo make this a priority queue - priority_queue
    // std::map<long,REQUESTQUEUE > RequestQueue;
    // typename std::map<long,REQUESTQUEUE >::iterator CurrentItr;
    int m_count;

    // ctor
    CRequestQueue() {
        m_count = 0;

        RequestQueue = new ConcurrentHashMap<Long, PriorityQueue<Command<T>>>();
        CurrentItr = null;
    }

    // Method invoked by the client through the ThreadPool proxy
    // for inserting the created Command object
    void queueRequest(Command<T> cmdPtr) {
        if (cmdPtr == null) {
            return;
        }


        // Get the key associcated with this command
        int key = cmdPtr.GetKey();
        // Check if there exist an entry in the map
        // printf("Trying to insert entry with key=%u\n",key);

        if (RequestQueue.containsKey(new Long(key))) {
            // if yes get the associated queue and insert
            // printf("Entry Exisit, pushing to end of queue\n");
            RequestQueue.get(new Long(key)).add(cmdPtr);

        } else {
            // else creare a queue and associate
            // printf("Entry Does not Exisit, Creating a map entry\n");
            PriorityQueue<Command<T>> reqQue = new PriorityQueue<Command<T>>(
                    1, (Comparator<Command<T>>) compare);
            reqQue.add(cmdPtr);
            RequestQueue.put(new Long(key), reqQue);
        }

        // Increment the total elements of the queue counter
        ++m_count;

        System.out.println("Total number of elements=" + m_count);

    }

    // This method is called by a thread when it is free to procees
    // a request. It deques the request -fifo
    Command<T> getRequest() throws Exception {
        PriorityQueue<Command<T>> tmpQueue;
        Long key = null;

        //critsec.lock();// Actually only one thread calls this at a time- so
        // this does not seem really needed

        try {
            // check if the map is empty
            if (RequestQueue.isEmpty()) {
                System.out.println("No elements are there in the map\n");
                return null;
            }

            if (CurrentItr == null) {
                CurrentItr = RequestQueue.keySet().iterator();
            }

            // The map is not empty so check where the current iterator points
            while (CurrentItr.hasNext() != false) {
                // not in the end so check the queue associated with this map
                // entry

                // To get the queue associated with this map
                key = (Long) CurrentItr.next();
                tmpQueue = RequestQueue.get(key);
                Iterator inner = tmpQueue.iterator();

                if (tmpQueue.isEmpty()) {
                    System.out.println("tmpQueue Queue Empty");
                    // if the current queue associated with this iter is iterate
                    // to the
                    // next node and see
                    key = (Long) CurrentItr.next();

                } else { // ok the queue associated with this is not empty
                    // so break
                    break;
                }
            }

            if (key == null) {
                CurrentItr = RequestQueue.keySet().iterator();
                return null;
            }

            // we reach here if the Currentitr is valid and also there is a non
            // empty
            // queue associated with this.

            // out the element
            Command<T> temp = null;

            tmpQueue = RequestQueue.get(key);

            if (tmpQueue == null) {
                System.out.println("Associated Queue Empty");
                return null;
            }
            temp = tmpQueue.poll();

            --m_count;
            // printf("**Total number of elements=%d\n",m_count);
            return temp;

        } catch (Exception e) {
            System.out.println("Exception Caught in GetRequest.....\n");
            throw e;
        } finally {
            //critsec.unlock();
        }

    }

    boolean empty()// todo
    {
        // printf("IsEmpty--Total number of elements=%d\n",m_count);

        if (m_count > 0) {
            return false;
        } else {
            return true;
        }

    }
};
