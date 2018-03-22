package org.bahmni.module.bahmniucc.controller;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Vector;

import static org.bahmni.module.bahmniucc.util.OpenERPUtils.DATABASE;
import static org.bahmni.module.bahmniucc.util.OpenERPUtils.PASSWORD;

/**
 * Created by ucc-ian on 15/Feb/2018.
 */
public class DeliveryOrderController {
    private static DeliveryOrderController ourInstance = new DeliveryOrderController();
    OpenERPUtils util = new OpenERPUtils();

    private Logger logger = org.apache.log4j.Logger.getLogger(getClass());
    public static DeliveryOrderController getInstance() {
        return ourInstance;
    }

    private DeliveryOrderController() {
    }


    public boolean updateDeliveryOrder(String drugName, String partnerId) {

        try {
            Object loginId = util.login();

            XmlRpcClient xmlrpcLogin = util.getXmlRpcClient();
            Object[] productArgs = new Object[1];
            Object[] name = new Object[3];
            name[0] = "name";
            name[1] = "=";
            name[2] = drugName;

            productArgs[0] = name;

            Object[] productParams = new Object[]{DATABASE, loginId, PASSWORD, "product.product", "search", productArgs};
            Object productResult = (Object[]) xmlrpcLogin.execute("execute", productParams);

            System.out.println("result value " + ((Object[]) productResult)[0]);

            Object[] stockMoveArgs = new Object[2];
            Object[] productId = new Object[3];
            productId[0] = "product_id";
            productId[1] = "=";
            productId[2] = ((Object[]) productResult)[0];

            Object[] partner_id = new Object[3];
            partner_id[0] = "partner_id";
            partner_id[1] = "=";
            partner_id[2] = partnerId;

            stockMoveArgs[0] = productId;
            stockMoveArgs[1] = partner_id;

            Object[] searchQuery = new Object[]{};
            Vector readqueryVector = new Vector();
            readqueryVector.addElement(searchQuery);
            Object[] stockMoveArg = new Object[]{DATABASE, loginId, PASSWORD, "stock.move", "search", stockMoveArgs};

            Object resultread = (Object[]) xmlrpcLogin.execute("execute", stockMoveArg);

            System.out.println("result string " + resultread.toString());

            Object[] a = (Object[]) resultread;

            System.out.println("result size " + a.length);

            if (a.length > 0) {
                System.out.println((int) a[0]);
                return writeStockMove(a[0], loginId);
            }

        } catch (XmlRpcException e) {
            System.out.println("XmlRpcException " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
            return false;

        }

        return false;
    }

    public boolean writeStockMove(Object stockMoveId, Object loginId) {
        XmlRpcClient xmlrpcClient = null;
        try {
            xmlrpcClient = util.getXmlRpcClient();


            HashMap<Object, Object> params = new HashMap<Object, Object>();
            params.put("state", "done");

            Vector<Object> arg = new Vector<Object>();
            arg.add("openerp");
            arg.add((int) loginId);
            arg.add(PASSWORD);
            arg.add("stock.move");
            arg.add("write");
            arg.add(stockMoveId);
            arg.add(params);

            Object ret_id = xmlrpcClient.execute("execute", arg);
            logger.info("Update stock.move state with id :" + ret_id.toString());
            return (boolean)ret_id;



        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        }

    }
}
