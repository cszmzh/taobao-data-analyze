package hashids.utils;

import org.hashids.Hashids;

/**
 * hashids编码和解码
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/3
 */
public class HashidsUtils {

    /**
     * 编码
     *
     * @param id
     * @return
     */
    public static String encode(long id) {
        Hashids hashids = new Hashids("github@bananaza", 8);
        return hashids.encode(id);
    }

    /**
     * 解码
     *
     * @param hasdids
     * @return
     */
    public static long decode(String hasdids) {
        try {
            Hashids hashids = new Hashids("github@bananaza", 8);
            return hashids.decode(hasdids)[0];
        } catch (Exception ex) {
            System.out.println("invalid id is " + hasdids);
            throw new IllegalArgumentException("invalid id is " + hasdids);
        }
    }
}
