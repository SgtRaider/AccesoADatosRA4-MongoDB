package com.raider.principal.model;

import com.mongodb.MongoClient;
import com.mongodb.MongoQueryException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.raider.principal.base.*;
import com.raider.principal.util.Values;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import jdk.internal.org.xml.sax.SAXException;
import org.bson.types.ObjectId;
import org.w3c.dom.*;
import org.bson.Document;
import raider.Util.Utilities;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by raider on 5/11/15.
 */
public class Projectmodel {

    private MongoClient mongoCliente;
    private MongoDatabase db;

    public void conexionMongo() {
        mongoCliente = new MongoClient();
        db = mongoCliente.getDatabase("ejercito");
    }

    public String login(String user, String contrasena) {

        if (Values.warningBaseDatos == false) {

            try {
                FindIterable<Document> findIterable = db.getCollection("usuarios").find(new Document().append("usuario", user).append("contraseña", contrasena));
                Document document = findIterable.first();

                if (document == null) {
                    Utilities.mensajeError("Error al hacer login, Usuario u contraseña incorrectos");
                    return null;
                }
                    return document.getString("rol");
            } catch (MongoQueryException mqe) {
                mqe.printStackTrace();
                Utilities.mensajeError("Error al hacer login");
                return null;
            }
        } else {
            Utilities.mensajeError("\tAvise al encargado del sistema.\n" +
                    "Login no activo debido a fallo de conexion con base de datos.\n" +
                    "Revise fichero de configuracion o compruebe si esta" +
                    " disponible la base de datos ejercito en el servidor.\n" +
                    "Tras solucionar el problema reinicie la aplicacion.");
            return null;
        }
    }

    public Unidad getUnidad(Document doc) {

        Unidad unidad = new Unidad();

        unidad.setId(doc.getObjectId("_id"));
        unidad.setnUnidad(doc.getString("nUnidad"));
        unidad.setnCuartel(doc.getString("nCuartel"));
        unidad.setTipo(doc.getString("tipo"));
        unidad.setFechaCreacion(doc.getDate("fechaCreacion"));
        unidad.setNoTropas(doc.getInteger("noTropas"));

        return unidad;
    }

    public Cuartel getCuartel(Document doc) {

        Cuartel cuartel = new Cuartel();

        cuartel.setId(doc.getObjectId("_id"));
        cuartel.setnCuartel(doc.getString("nCuartel"));
        cuartel.setLatitud(doc.getDouble("latitud"));
        cuartel.setLongitud(doc.getDouble("longitud"));
        cuartel.setActividad(doc.getBoolean("actividad"));
        cuartel.setLocalidad(doc.getString("localidad"));

        return cuartel;
    }

    public Soldado getSoldado(Document doc) {

        Soldado soldado = new Soldado();

        soldado.setNombre(doc.getString("nombre"));
        soldado.setApellidos(doc.getString("apellidos"));
        soldado.setRango(doc.getString("rango"));
        soldado.setnUnidad(doc.getString("nUnidad"));
        soldado.setLugarNacimiento(doc.getString("lugarNacimiento"));
        soldado.setFechaNacimiento(doc.getDate("fechaNacimiento"));
        soldado.setId(doc.getObjectId("_id"));

        return soldado;
    }

    public Soldado getSoldado(ObjectId id) {

        FindIterable<Document> findIterable = db.getCollection("soldado").find(new Document("_id",id));
        Document document = findIterable.first();

        return getSoldado(document);
    }

    public Unidad getUnidad(ObjectId id) {

        FindIterable<Document> findIterable = db.getCollection("unidad").find(new Document("_id",id));
        Document document = findIterable.first();

        return getUnidad(document);
    }

    public Cuartel getCuartel(ObjectId id) {

        FindIterable<Document> findIterable = db.getCollection("cuartel").find(new Document("_id",id));
        Document document = findIterable.first();

        return getCuartel(document);
    }

    public Document getDoc(Unidad unidad, int op) {
        Document doc = new Document()
                .append("nUnidad", unidad.getnUnidad())
                .append("nCuartel", unidad.getnCuartel())
                .append("noTropas", unidad.getNoTropas())
                .append("tipo", unidad.getTipo())
                .append("fechaCreacion", unidad.getFechaCreacion());
        if (op == 1) doc.append("_id", unidad.getId());
        return doc;
    }

    public Document getDoc(Cuartel cuartel, int op) {
        Document doc = new Document()
                .append("nCuartel", cuartel.getnCuartel())
                .append("latitud", cuartel.getLatitud())
                .append("longitud", cuartel.getLongitud())
                .append("localidad", cuartel.getLocalidad())
                .append("actividad", cuartel.getActividad());
        if (op == 1) doc.append("_id", cuartel.getId());
        return doc;
    }

    public Document getDoc(Soldado soldado, int op) {
        Document doc = new Document()
                .append("nombre", soldado.getNombre())
                .append("apellidos", soldado.getApellidos())
                .append("rango", soldado.getRango())
                .append("nUnidad", soldado.getnUnidad())
                .append("fechaNacimiento", soldado.getFechaNacimiento())
                .append("lugarNacimiento", soldado.getLugarNacimiento());
        if (op == 1) doc.append("_id", soldado.getId());
        return doc;
    }

    public void guardarCuartel(Document doc) {
        db.getCollection("cuartel").insertOne(doc);
    }

    public void guardarUnidad(Document doc) {
        db.getCollection("unidad").insertOne(doc);
    }

    public void guardarSoldado(Document doc) {
        db.getCollection("soldado").insertOne(doc);
    }

    public void modificarCuartel(ObjectId id, Document doc) {
        db.getCollection("cuartel").replaceOne(new Document("_id", id), doc);
    }

    public void modificarUnidad(ObjectId id, Document doc) {
        db.getCollection("unidad").replaceOne(new Document("_id", id), doc);
    }

    public void modificarSoldado(ObjectId id, Document doc) {
        db.getCollection("soldado").replaceOne(new Document("_id", id), doc);
    }

    public void eliminarCuartel(Document doc) {
        db.getCollection("cuartel").deleteOne(doc);
    }

    public void eliminarUnidad(Document doc) {
        db.getCollection("unidad").deleteOne(doc);
    }

    public void eliminarSoldado(Document doc) {
        db.getCollection("soldado").deleteOne(doc);
    }

    private List listarUnidad() {
        List lista = new ArrayList<>();
        FindIterable findIterable = db.getCollection("unidad").find();
        Iterator<Document> iterator = findIterable.iterator();

        while(iterator.hasNext()) {
            Document document = iterator.next();
            lista.add(getUnidad(document));
        }

        return lista;
    }

    private List listarCuartel() {
        List lista = new ArrayList<>();
        FindIterable findIterable = db.getCollection("cuartel").find();
        Iterator<Document> iterator = findIterable.iterator();

        while(iterator.hasNext()) {
            Document document = iterator.next();
            lista.add(getCuartel(document));
        }

        return lista;
    }

    private List listarSoldado() {
        List lista = new ArrayList<>();
        FindIterable findIterable = db.getCollection("soldado").find();
        Iterator<Document> iterator = findIterable.iterator();

        while(iterator.hasNext()) {
            Document document = iterator.next();
            lista.add(getSoldado(document));
        }

        return lista;
    }

    public List<Object[]> listarGeneral(String clase) {

        List lista = new ArrayList<>();
        if (clase.equalsIgnoreCase("unidad")) {

            lista = listarUnidad();
            List<Object[]> listaObjetos = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {

                Object[] objects = new Object[]{((Unidad) lista.get(i)).getId(),
                        ((Unidad) lista.get(i)).getnUnidad(), ((Unidad) lista.get(i)).getTipo(),
                        ((Unidad) lista.get(i)).getNoTropas(), ((Unidad) lista.get(i)).getFechaCreacion(), ((Unidad) lista.get(i)).getnCuartel()};
                listaObjetos.add(objects);
            }
            return listaObjetos;
        } else {

            if(clase.equalsIgnoreCase("soldado")) {

                lista = listarSoldado();
                List<Object[]> listaObjetos = new ArrayList<>();
                for (int i = 0; i < lista.size(); i++) {
                    Object[] objects = new Object[]{((Soldado) lista.get(i)).getId(),
                            ((Soldado) lista.get(i)).getNombre(), ((Soldado) lista.get(i)).getApellidos(),
                            ((Soldado) lista.get(i)).getRango(), ((Soldado) lista.get(i)).getFechaNacimiento(),
                            ((Soldado) lista.get(i)).getLugarNacimiento(),
                            ((Soldado) lista.get(i)).getnUnidad()};
                    listaObjetos.add(objects);


                }
                return listaObjetos;
            } else {


                if (clase.equalsIgnoreCase("cuartel")) {

                    lista = listarCuartel();
                    List<Object[]> listaObjetos = new ArrayList<>();
                    for (int i = 0; i < lista.size(); i++) {

                        Object[] objects = new Object[]{((Cuartel) lista.get(i)).getId(),
                                ((Cuartel) lista.get(i)).getnCuartel(),((Cuartel) lista.get(i)).getLocalidad(), ((Cuartel) lista.get(i)).getLatitud(),
                                ((Cuartel) lista.get(i)).getLongitud(), ((Cuartel) lista.get(i)).getActividad()};
                        listaObjetos.add(objects);


                    }
                    return listaObjetos;
                }
            }
        }

        return null;
    }

    public List<String> actualizarCombo(String tabla) {
        List<String> lista = new ArrayList<>();
        if(tabla.equalsIgnoreCase("Cuartel")) {

            FindIterable findIterable = db.getCollection("cuartel").find();
            Iterator<Document> iterator = findIterable.iterator();

            while(iterator.hasNext()) {
                Document document = iterator.next();
                lista.add(getCuartel(document).getnCuartel());
            }

        } else {

            if (tabla.equalsIgnoreCase("Unidad")) {

                FindIterable findIterable = db.getCollection("unidad").find();
                Iterator<Document> iterator = findIterable.iterator();

                while(iterator.hasNext()) {
                    Document document = iterator.next();
                    lista.add(getUnidad(document).getnUnidad());
                }
            }
        }
        return lista;
    }

    public List<Object[]> buscarCuartel(String busqueda) {

        List lista = new ArrayList<>();
        FindIterable findIterable = db.getCollection("cuartel").find(new Document("$or", Arrays.asList(
                new Document("nCuartel", busqueda),
                new Document("localidad", busqueda)
        )));
        Iterator<Document> iterator = findIterable.iterator();
        while(iterator.hasNext()) {
            Document document = iterator.next();
            lista.add(getCuartel(document));
        }
        List<Object[]> listaObjetos = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {

            Object[] objects = new Object[]{((Cuartel) lista.get(i)).getId(),
                    ((Cuartel) lista.get(i)).getnCuartel(),((Cuartel) lista.get(i)).getLocalidad(), ((Cuartel) lista.get(i)).getLatitud(),
                    ((Cuartel) lista.get(i)).getLongitud(), ((Cuartel) lista.get(i)).getActividad()};
            listaObjetos.add(objects);


        }
        return listaObjetos;
    }

    public List<Object[]> buscarUnidad(String busqueda) {
        //TODO
        List lista = new ArrayList<>();
        FindIterable findIterable = db.getCollection("unidad").find(new Document("$or", Arrays.asList(
                new Document("nCuartel", busqueda),
                new Document("nUnidad", busqueda),
                new Document("tipo", busqueda)
        )));
        Iterator<Document> iterator = findIterable.iterator();
        while(iterator.hasNext()) {
            Document document = iterator.next();
            lista.add(getUnidad(document));
        }
        List<Object[]> listaObjetos = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {

            Object[] objects = new Object[]{((Unidad) lista.get(i)).getId(),
                    ((Unidad) lista.get(i)).getnUnidad(), ((Unidad) lista.get(i)).getTipo(),
                    ((Unidad) lista.get(i)).getNoTropas(), ((Unidad) lista.get(i)).getFechaCreacion(), ((Unidad) lista.get(i)).getnCuartel()};
            listaObjetos.add(objects);
        }
        return listaObjetos;
    }

    public List<Object[]> buscarSoldado(String busqueda) {
        //TODO
        List lista = new ArrayList<>();
        FindIterable findIterable = db.getCollection("soldado").find(new Document("$or", Arrays.asList(
                new Document("nombre", busqueda),
                new Document("nUnidad", busqueda),
                new Document("apellidos", busqueda),
                new Document("rango", busqueda)
        )));
        Iterator<Document> iterator = findIterable.iterator();
        while(iterator.hasNext()) {
            Document document = iterator.next();
            lista.add(getSoldado(document));
        }
        List<Object[]> listaObjetos = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            Object[] objects = new Object[]{((Soldado) lista.get(i)).getId(),
                    ((Soldado) lista.get(i)).getNombre(), ((Soldado) lista.get(i)).getApellidos(),
                    ((Soldado) lista.get(i)).getRango(), ((Soldado) lista.get(i)).getFechaNacimiento(),
                    ((Soldado) lista.get(i)).getLugarNacimiento(),
                    ((Soldado) lista.get(i)).getnUnidad()};
            listaObjetos.add(objects);
        }
        return listaObjetos;
    }

    public ArrayList<Object> prepararExportar() {
        ArrayList<Object> pack = new ArrayList<>();
        pack.add(listarCuartel());
        pack.add(listarUnidad());
        pack.add(listarSoldado());
        return pack;
    }

    public void cargarImport(ArrayList<Object> pack) {

        List<Cuartel> lc = (List<Cuartel>) pack.get(0);
        List<Unidad> lu = (List<Unidad>) pack.get(1);
        List<Soldado> ls = (List<Soldado>) pack.get(2);

        for (Cuartel cuartel: lc) {
            guardarCuartel(getDoc(cuartel,0));
        }

        for (Unidad unidad: lu) {
            guardarUnidad(getDoc(unidad,0));
        }

        for (Soldado soldado: ls) {
            guardarSoldado(getDoc(soldado,0));
        }
    }

    // Metodo que exporta a XML los objetos, en una ruta determinada

    public void exportar(String path) throws ParserConfigurationException,
            TransformerException {
        ArrayList<Object> pack = prepararExportar();
        List<Cuartel> lc = (List<Cuartel>) pack.get(0);
        List<Unidad> lu = (List<Unidad>) pack.get(1);
        List<Soldado> ls = (List<Soldado>) pack.get(2);

        DateFormat format = new SimpleDateFormat("dd MM yyyy");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document doc;

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        DOMImplementation dom = docBuilder.getDOMImplementation();
        doc = dom.createDocument(null, "xml", null);

        Element root = doc.createElement("archivo");
        Element cuarteles = doc.createElement("cuarteles");
        Element unidades = doc.createElement("unidades");
        Element soldados = doc.createElement("soldados");

        doc.getDocumentElement().appendChild(root);
        root.appendChild(cuarteles);
        root.appendChild(unidades);
        root.appendChild(soldados);

        Element nodoCuartel = null, nodoUnidad = null,
                nodoSoldado = null, nodoDatos = null;
        Text txt = null;

        for (Cuartel cuartel : lc) {

            nodoCuartel = doc.createElement("cuartel");
            cuarteles.appendChild(nodoCuartel);

            nodoDatos = doc.createElement("nombre_cuartel");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(cuartel.getnCuartel());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("localidad");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(cuartel.getLocalidad());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("latitud");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(cuartel.getLatitud()));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("longitud");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(cuartel.getLongitud()));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("activo");
            nodoCuartel.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(cuartel.getActividad()));
            nodoDatos.appendChild(txt);
        }

        for (Unidad unidad : lu) {

            nodoUnidad = doc.createElement("unidad");
            unidades.appendChild(nodoUnidad);

            nodoDatos = doc.createElement("nombre_unidad");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(unidad.getnUnidad());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("tipo");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(unidad.getTipo());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("n_cuartel");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(unidad.getnCuartel());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("no_tropas");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(unidad.getNoTropas()));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("fecha_creacion");
            nodoUnidad.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(format.format(unidad.getFechaCreacion())));
            nodoDatos.appendChild(txt);
        }

        for (Soldado soldado : ls) {

            nodoSoldado = doc.createElement("soldado");
            soldados.appendChild(nodoSoldado);

            nodoDatos = doc.createElement("nombre");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getNombre());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("apellidos");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getApellidos());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("lugar_nacimiento");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getLugarNacimiento());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("fecha_nacimiento");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(String.valueOf(format.format(soldado.getFechaNacimiento())));
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("rango");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getRango());
            nodoDatos.appendChild(txt);

            nodoDatos = doc.createElement("n_unidad");
            nodoSoldado.appendChild(nodoDatos);
            txt = doc.createTextNode(soldado.getnUnidad()); //FIXME Arreglar
            nodoDatos.appendChild(txt);
        }

        Source source = new DOMSource(doc);
        Result resultado = new StreamResult(new File(path));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, resultado);
    }

    // Metodo que importa un XML de una ruta determinada, y lo transforma a en un paquete
    // para poder cargarlo mas tarde

    public ArrayList<Object> importar(String path) throws ParserConfigurationException, IOException, ParseException, org.xml.sax.SAXException {

        ArrayList<Object> pack = new ArrayList();
        ArrayList<Cuartel> lcuartel = new ArrayList<>();
        ArrayList<Unidad> lunidad = new ArrayList<>();
        ArrayList<Soldado> lsoldado = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd MM yyyy");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document doc = null;

        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(new File(path));

        // Extraccion de los datos Cuartel

        NodeList cuarteles = doc.getElementsByTagName("cuartel");
        for (int i = 0; i < cuarteles.getLength(); i++) {
            Node cuartel = cuarteles.item(i);
            Element elemento = (Element) cuartel;

            Cuartel c = new Cuartel();

            c.setnCuartel(elemento.getElementsByTagName("nombre_cuartel").item(0)
                    .getChildNodes().item(0).getNodeValue());
            c.setLocalidad(elemento.getElementsByTagName("localidad").item(0)
                    .getChildNodes().item(0).getNodeValue());
            c.setLatitud(Double.valueOf(elemento.getElementsByTagName("latitud").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            c.setLongitud(Double.valueOf(elemento.getElementsByTagName("longitud").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            c.setActividad(Boolean.valueOf(elemento.getElementsByTagName("activo").item(0)
                    .getChildNodes().item(0).getNodeValue()));

            lcuartel.add(c);
        }

        // Extraccion de los datos UnidadView

        NodeList unidades = doc.getElementsByTagName("unidad");
        for (int i = 0; i < unidades.getLength(); i++) {
            Node unidad = unidades.item(i);
            Element elemento = (Element) unidad;

            Unidad u = new Unidad();

            u.setnUnidad(elemento.getElementsByTagName("nombre_unidad").item(0)
                    .getChildNodes().item(0).getNodeValue());
            u.setTipo(elemento.getElementsByTagName("tipo").item(0)
                    .getChildNodes().item(0).getNodeValue());
            u.setnCuartel(elemento.getElementsByTagName("n_cuartel").item(0)
                    .getChildNodes().item(0).getNodeValue());

            u.setNoTropas(Integer.valueOf(elemento.getElementsByTagName("no_tropas").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            u.setFechaCreacion(format.parse(elemento.getElementsByTagName("fecha_creacion").item(0)
                    .getChildNodes().item(0).getNodeValue()));

            lunidad.add(u);
        }


        NodeList soldados = doc.getElementsByTagName("soldado");
        for (int i = 0; i < soldados.getLength(); i++) {
            Node soldado = soldados.item(i);
            Element elemento = (Element) soldado;

            Soldado s = new Soldado();

            s.setNombre(elemento.getElementsByTagName("nombre").item(0)
                    .getChildNodes().item(0).getNodeValue());
            s.setApellidos(elemento.getElementsByTagName("apellidos").item(0)
                    .getChildNodes().item(0).getNodeValue());
            s.setnUnidad(elemento.getElementsByTagName("n_unidad").item(0)
                    .getChildNodes().item(0).getNodeValue());
            s.setRango(elemento.getElementsByTagName("rango").item(0)
                    .getChildNodes().item(0).getNodeValue());
            s.setFechaNacimiento(format.parse(elemento.getElementsByTagName("fecha_nacimiento").item(0)
                    .getChildNodes().item(0).getNodeValue()));
            s.setLugarNacimiento(elemento.getElementsByTagName("lugar_nacimiento").item(0)
                    .getChildNodes().item(0).getNodeValue());

            lsoldado.add(s);
        }


        pack.add(lcuartel);
        pack.add(lunidad);
        pack.add(lsoldado);

        return pack;
    }
}
