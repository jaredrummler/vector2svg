/*
 * Copyright (C) 2015. Jared Rummler <me@jaredrummler.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Vector2Svg {

  public static void main(String[] args) throws Exception {
    if (args == null || args.length == 0) {
      printUsage();
      return;
    }

    for (String path : args) {
      File xml = new File(path);
      if (!xml.exists() || !xml.canRead()) {
        System.out.println(xml.getPath() + " does not exist.");
        continue;
      }
      File svg = new File(xml.getParent(), xml.getName().replaceFirst("[.][^.]+$", ".svg"));
      try {
        AndroidVectorDrawable drawable = getDrawable(xml);
        vectorToSvgFile(drawable, svg);
      } catch (Exception e) {
        System.out.println("Error creating SVG from " + xml.getName());
      }
    }
  }

  private static void vectorToSvgFile(AndroidVectorDrawable drawable, File destination)
      throws ParserConfigurationException, TransformerException {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element svg = doc.createElement("svg");
    svg.setAttribute("viewBox", "0 0 " + drawable.getWidth() + " " + drawable.getHeight());
    for (VectorPath path : drawable.paths) {
      Element child = doc.createElement("path");
      if (path.fillColor != null) {
        child.setAttribute("fill", path.fillColor);
      }
      child.setAttribute("d", path.pathData);
      svg.appendChild(child);
    }
    doc.appendChild(svg);
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(destination);
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    transformer.transform(source, result);
  }

  private static AndroidVectorDrawable getDrawable(File file)
      throws ParserConfigurationException, IOException, SAXException {
    Document xml =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    xml.getDocumentElement().normalize();
    Node vector = xml.getElementsByTagName("vector").item(0);
    NamedNodeMap attributes = vector.getAttributes();
    NodeList children = vector.getChildNodes();
    int width, height;
    width = getWidth(attributes);
    height = getHeight(attributes);
    List<VectorPath> paths = getPaths(children);
    return new AndroidVectorDrawable(paths, width, height);
  }

  private static int getWidth(NamedNodeMap attributes) {
    for (int i = 0; i < attributes.getLength(); i++) {
      if (attributes.item(i).getNodeName().equals("android:viewportWidth")) {
        return (int) Double.parseDouble(attributes.item(i).getNodeValue());
      }
    }
    return 0;
  }

  private static int getHeight(NamedNodeMap attributes) {
    for (int i = 0; i < attributes.getLength(); i++) {
      if (attributes.item(i).getNodeName().equals("android:viewportHeight")) {
        return (int) Double.parseDouble(attributes.item(i).getNodeValue());
      }
    }
    return 0;
  }

  private static List<VectorPath> getPaths(NodeList children) {
    List<VectorPath> paths = new ArrayList<>();
    for (int i = 0; i < children.getLength(); i++) {
      Node item = children.item(i);
      if (item.getNodeName().equals("path")) {
        String pathData = null;
        String fillColor = null;
        for (int j = 0; j < item.getAttributes().getLength(); j++) {
          Node node = item.getAttributes().item(j);
          switch (node.getNodeName()) {
            case "android:pathData":
              pathData = node.getNodeValue();
              break;
            case "android:fillColor":
              fillColor = node.getNodeValue();
              break;
          }
        }
        if (pathData != null) {
          paths.add(new VectorPath(pathData, fillColor));
        }
      }
    }
    return paths;
  }

  static class VectorPath {

    String pathData;
    String fillColor;

    public VectorPath(String pathData, String fillColor) {
      this.pathData = pathData;
      this.fillColor = fillColor;
    }
  }

  static class AndroidVectorDrawable {

    List<VectorPath> paths;
    int height;
    int width;

    public AndroidVectorDrawable(List<VectorPath> paths, int width, int height) {
      this.paths = paths;
      this.height = height;
      this.width = width;
    }

    String getHeight() {
      return Integer.toString(height);
    }

    String getWidth() {
      return Integer.toString(height);
    }
  }

  private static void printUsage() {
    File jarFile =
        new File(Vector2Svg.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    System.out.println("Convert Android VectorDrawable XML resource file to SVG");
    System.out.println();
    System.out.println(String.format("Usage: java -jar %s [FILE]...", jarFile.getName()));
  }
}
