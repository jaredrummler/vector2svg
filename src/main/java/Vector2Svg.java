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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Vector2Svg {

  public static void main(String[] args) {
    if (args == null || args.length == 0 || args[0].equals("--help")) {
      printUsage();
      return;
    }
    for (String path : args) {
      Vector2Svg converter = new Vector2Svg(new File(path));
      if (!converter.createSvg()) {
        System.out.println("Error creating SVG from " + path);
      }
    }
  }

  private static void printUsage() {
    File jarFile =
        new File(Vector2Svg.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    System.out.println("Convert Android VectorDrawable XML resource file to SVG");
    System.out.println();
    System.out.println(String.format("Usage: java -jar %s [FILE]...", jarFile.getName()));
  }

  private final File source;
  private final File destination;

  public Vector2Svg(File source) {
    this(source, new File(source.getParent(), source.getName().replaceFirst("[.][^.]+$", ".svg")));
  }

  public Vector2Svg(File source, File destination) {
    this.source = source;
    this.destination = destination;
  }

  public boolean createSvg() {
    try {
      AndroidVectorDrawable drawable = getDrawable();
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element svg = doc.createElement("svg");
      svg.setAttribute("viewBox", String.format("0 0 %.1f %.1f", drawable.width, drawable.height));
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
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private AndroidVectorDrawable getDrawable()
      throws ParserConfigurationException, IOException, SAXException {
    Document xml =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(source);
    xml.getDocumentElement().normalize();
    Node vector = xml.getElementsByTagName("vector").item(0);
    NamedNodeMap attributes = vector.getAttributes();
    NodeList children = vector.getChildNodes();

    double width = 0;
    double height = 0;
    for (int i = 0; i < attributes.getLength(); i++) {
      if (attributes.item(i).getNodeName().equals("android:viewportHeight")) {
        height = Double.parseDouble(attributes.item(i).getNodeValue());
      } else if (attributes.item(i).getNodeName().equals("android:viewportWidth")) {
        width = Double.parseDouble(attributes.item(i).getNodeValue());
      }
    }

    List<VectorPath> paths = new ArrayList<>();
    for (int i = 0; i < children.getLength(); i++) {
      Node item = children.item(i);
      if (item.getNodeName().equals("path")) {
        String pathData = null;
        String fillColor = null;
        for (int j = 0; j < item.getAttributes().getLength(); j++) {
          Node node = item.getAttributes().item(j);
          String name = node.getNodeName();
          String value = node.getNodeValue();
          if (name.equals("android:pathData")) {
            pathData = value;
          } else if (name.equals("android:fillColor") && value.startsWith("#")) {
            fillColor = value;
          }
        }
        if (pathData != null) {
          paths.add(new VectorPath(pathData, fillColor));
        }
      }
    }

    return new AndroidVectorDrawable(paths, width, height);
  }

  private class VectorPath {

    private String pathData;
    private String fillColor;

    private VectorPath(String pathData, String fillColor) {
      this.pathData = pathData;
      this.fillColor = fillColor;
    }
  }

  private class AndroidVectorDrawable {

    private final List<VectorPath> paths;
    private final double height;
    private final double width;

    private AndroidVectorDrawable(List<VectorPath> paths, double width, double height) {
      this.paths = paths;
      this.height = height;
      this.width = width;
    }
  }

}
