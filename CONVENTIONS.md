# Convenciones y ordenamiento de codigo y archivos

## <a name='tabla-de-contenidos'>Tabla de contenidos</a>

1. [Reglas generales](#reglas)
2. [Back End](#backend)
   + [Organizacion de Archivos](#backend-archivos)
   + [Convenciones de codigo](#backend-codigo)
4. [Front End](#frontend)
   + [Organizacion de Archivos](#frontend-archivos)
     + [Carpeta src/assets](#frontend-archivos-assets)
     + [Carpeta src/components](#frontend-archivos-components)
     + [Carpeta src/models](#frontend-archivos-models)
     + [Carpeta src/services](#frontend-archivos-services)
   + [Typescript, React, Componentes](#frontend-general)
     + [Importaciones](#frontend-general-importaciones)
     + [Interfaces](#frontend-general-interfaces)
     + [Componentes](#frontend-general-componentes)
       + [Nombres y orden dentro del Componente](#frontend-general-componentes-nombres)
       + [Renderizado del componente](#frontend-general-componentes-renderizado)
       + [Reglas para classNames](#frontend-general-componentes-classnames) 
     + [Exportaciones](#frontend-general-exportaciones)

## <a name="reglas">Reglas generales</a>

Las convenciones de desarrollo son reglas y pautas que el equipo de desarrollo del proyecto "El Buen Sabor" debe seguir al escribir código y organizar archivos. Estas convenciones proporcionan una estructura coherente y unificada en todo el proyecto, lo que facilita la comprensión y colaboración entre los miembros del equipo.

Es importante tener en cuenta que las convenciones pueden cambiar según las necesidades del equipo de desarrollo. A medida que el proyecto evoluciona y se enfrenta a nuevos desafíos, pueden surgir nuevas convenciones o pueden ser necesarios ajustes en las existentes. Es fundamental que el equipo mantenga una comunicación abierta y discuta las convenciones en consecuencia para adaptarse a los requisitos cambiantes del proyecto y garantizar la eficacia en el desarrollo.

## <a name='backend'>Back End</a>

Se debe tener en cuenta que los nombres de los archivos, funciones, metodos, variables y atributos en el Back End deberan estar escritos en ingles, exceptuando comentarios, exceptions, tests, etc.


**[[⬆ Subir]](#tabla-de-contenidos)**


## <a name='backend-archivos'>Organizacion de Archivos</a>

En el proyecto, se utilizará una estructura de carpetas para organizar los archivos de acuerdo con su función y responsabilidad. 
A continuación, se describen cómo se distribuirán los archivos en las carpetas controllers, models, services y repositories:

### Carpeta Controllers

La carpeta controllers contendrá los archivos que manejan las rutas de la API y las acciones correspondientes. Estos archivos son responsables de recibir las solicitudes HTTP, interactuar con los modelos y servicios apropiados, y devolver las respuestas adecuadas.

Algunas funciones y cosas que podrían ubicarse en esta carpeta incluyen:

+ Definición de las rutas de la API y su vinculación con las funciones correspondientes.
+ Validación de datos recibidos en las solicitudes HTTP.
+ Lógica para manejar las solicitudes, procesar los datos y preparar las respuestas.

Ejemplo:
```
// http://localhost:4000/api/categories/all
@GetMapping("/all")
public List<CategoryModel> getCategories() {
  return this.categoryService.getCategories();
}
```

### Carpeta Models

La carpeta models contendrá los archivos que representan las estructuras de datos del proyecto. Estos archivos definen los esquemas y las interacciones con la base de datos o cualquier otra fuente de datos utilizada en el proyecto.

Algunas funciones y cosas que podrían ubicarse en esta carpeta incluyen:

+ Definición de los modelos de datos utilizando algún ORM (Object-Relational Mapping) o biblioteca similar.
+ Definición de relaciones entre modelos si se utiliza una base de datos relacional.
+ Lógica para realizar consultas y manipular los datos de la base de datos.


Ejemplo:
```
@Entity(name = "Category")
@Table(name = "Category")
public class CategoryModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_category")
  private long id;
    
  @Column(name = "name")
  private String name;
    
  // constructores
  // getters & setters
  // metodos de la clase
}
```

### Carpeta Services

La carpeta services contendrá los archivos que encapsulan la lógica de negocio y proporcionan funcionalidades adicionales al proyecto. Estos archivos contienen funciones reutilizables que pueden ser utilizadas por los controladores u otros componentes del proyecto. 

Algunas funciones y cosas que podrían ubicarse en esta carpeta incluyen:

+ Implementación de la lógica de negocio específica del proyecto.
+ Funciones auxiliares y utilitarias que se utilizan en varias partes del proyecto.
+ Interacciones con servicios externos, como el envío de correos electrónicos, procesamiento de pagos, integraciones con API de terceros, etc.

Ejemplo:
```
public CategoryModel saveCategoryBD(CategoryModel category) {
  return this.categoryRepository.save(category);
}
```

### Carpeta Repositories

La carpeta repositories contendrá los archivos que se encargan de interactuar directamente con la capa de persistencia de datos. Estos archivos contienen funciones y consultas específicas para realizar operaciones de lectura y escritura en la base de datos.

Algunas funciones y cosas que podrían ubicarse en esta carpeta incluyen:

+ Definición de consultas y operaciones de bases de datos específicas.
+ Lógica para ejecutar consultas SQL o interactuar con un ORM.
+ Acceso a datos y manipulación de la capa de persistencia.

Ejemplo:
```
@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {
  @Query("SELECT r FROM Category r WHERE r.state=true")
  public List<RubroModel> listOfActivesCategories();
}
```


**[[⬆ Subir]](#tabla-de-contenidos)**


## <a name='backend-codigo'>Convenciones de Codigo</a>

### Nombres de Archivos
Los nombres de los archivos y las clases deben seguir el estilo PascalCase. Esto implica que cada palabra comienza con una letra mayúscula, incluida la primera palabra.

Ejemplo:
```
CategoryModel
ProductController
IngredientService
```

En caso de los archivos que se guarden en la carpeta repositories se pondra en al principio del nombre la letra "I", luego el nombre seguira con el estilo PascalCase y se terminara el nombre poniendo Repository.

Ejemplo:
```
ICategoryRepository
IIngredientRepository
IProductRepository
```


**[[⬆ Subir]](#tabla-de-contenidos)**


### Nombres de atributos, variables y funciones.
#### Atributos
Los nombres de los atributos de los modelos deben seguir un formato de snake_case.

Ejemplo:
```
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_product")
  private long id;

  @Column(name = "id_category_fk")
  private String nombre;

  @Column(name = "product_description")
  private boolean product_description;
```

#### Variables
Los nombres de las variables deben seguir un formato de camelCase, esto implica que la primera letra de cada palabra se escribe en mayúscula, excepto la primera palabra.

Ejemplo:
```
public boolean exampleFunction throws Exception {
  Optional<CategoryModel> categoryOptional = categoryRepository.findById(id);

  RubroModel category = categoryOptional.orElseThrow(() -> new Exception("No se encontro la categoria con id: " + id));

  if (category.isStatae() == false) {
     category.setState(true);
  }

  categoryRepository.save(category);

  return category.isState();
}
```

#### Funciones
Los nombres de las funciones deben seguir un formato de camelCase, esto implica que la primera letra de cada palabra se escribe en mayúscula, excepto la primera palabra.

Ejemplo:
```
public boolean exampleFunction throws Exception {
  // Codigo de la funcion
}

public void exampleMethodCamelCase throws Exception {
  // Codigo del metodo
}
```


**[[⬆ Subir]](#tabla-de-contenidos)**


## <a name='frontend'>Front End</a>
Se debe tener en cuenta que los nombres de los archivos y el codigo en el Front End deberan estar escritos en ingles, exceptuando comentarios, alertas y texto referido a la pagina en si.

## <a name='frontend-archivos'>Organizacion de Archivos</a>

### <a name='frontend-archivos-assets'>Carpeta Assets</a>
La carpeta assets contendrá recursos estáticos utilizados en la pagina, como imágenes, archivos de audio, fuentes, etc. En esta carpeta se deberan hacer sub-carpetas que contengan a otras, para tener una mejor organizacion de los assets.

Ejemplo: 
```
assets/images/carouselHome/image1.png
assets/logos/logo1.svg
assets/sounds/sound1.mp3
```


**[[⬆ Subir]](#tabla-de-contenidos)**


### <a name='frontend-archivos-components'>Carpeta Components</a>
La carpeta components se utilizara para guardar los componentes del proyecto, pero esta carpeta esta conformada por 3 sub-carpetas, a continuacion se definira que ira dentro de cada carpeta


**[[⬆ Subir]](#tabla-de-contenidos)**


#### Carpeta components/common:
La carpeta components/common contendrá componentes reutilizables que pueden ser utilizados en varias partes de la aplicación. Estos componentes suelen ser de uso general y no están directamente relacionados con una página específica.

Algunos ejemplos de componentes que podrían ubicarse en esta carpeta incluyen:
```
Botones reutilizables.
Barras de navegación.
Componentes de entrada de texto.
Alertas y notificaciones.
Componentes de carga o carga de datos.
```

#### Carpeta components/pages:
La carpeta components/pages contendrá componentes específicos de cada página de la aplicación. Estos componentes están diseñados para representar y controlar la interfaz de usuario de una página en particular. Cada página debe tener su propia carpeta dentro de components/pages para organizar sus componentes relacionados.

Algunos ejemplos de componentes que podrían ubicarse en esta carpeta incluyen:
```
Componentes de inicio de sesión.
Componentes de registro.
Componentes del carrito de compras.
Componentes del panel de administración.
```

#### Carpeta components/shared:
La carpeta components/shared contendrá componentes compartidos entre varias páginas de la aplicación. Estos componentes son específicos de la lógica de la aplicación y pueden ser utilizados por múltiples páginas.

Algunos ejemplos de componentes que podrían ubicarse en esta carpeta incluyen:
```
Componentes de autenticación (por ejemplo, formulario de inicio de sesión).
Componentes de navegación comunes (por ejemplo, barra lateral).
Componentes de diseño reutilizables (por ejemplo, encabezados, pies de página, etc.).
Componentes de gestión de estado global (por ejemplo, contenedores de contexto).
```


### <a name='frontend-archivos-models'>Carpeta Models</a>

La carpeta models/ contendra los archivo .ts, que deben representar un modelo de datos específico utilizado en la aplicación. Estos modelos pueden ser clases o interfaces que definan la estructura y las propiedades de los datos.

Ejemplo:
```
models/Category.ts
models/Product.ts
```


**[[⬆ Subir]](#tabla-de-contenidos)**


### <a name='frontend-archivos-services'>Carpeta Services</a>

La carpeta services contendrá archivos relacionados con la lógica de servicios y comunicación con servidores o API externas.

Cada archivo dentro de la carpeta services debe representar un servicio o una funcionalidad específica. 

Ejemplo:
```
Un archivo puede ser responsable de manejar la autenticación.
Otro para gestionar las solicitudes a la API de productos, etc.
```


**[[⬆ Subir]](#tabla-de-contenidos)**


## <a name='frontend-general'>Typescript, React, Componentes</a>

Se debe tener en cuenta que los nombres de los archivos, funciones, metodos, variables y atributos en el Back End deberan estar escritos en ingles, exceptuando comentarios, exceptions, tests, etc.

### Nombres de archivos

Los nombres de los archivos y las clases deben seguir el estilo PascalCase. Esto implica que cada palabra comienza con una letra mayúscula, incluida la primera palabra, luego del nombre debera tener el tipo de archivo ya sea .ts si es TypeScript y en caso de que sea un componente .tsx.


**[[⬆ Subir]](#tabla-de-contenidos)**


### <a name='frontend-general-importaciones'>Importaciones</a>

Las importaciones estaran ordenadas en grupos y a su vez estos grupos estaran ordenados, para mejorar la legibilidad del codigo.

A continuacion se muestra como estaran ordenados normalmente los grupos de imports(puede variar pero se debe cumplir con el estilo de ordenamiento):
```
// Importaciones de dependencias
import { useEffect, useState } from "react";
import { BsCircleFill } from "react-icons/bs";
import { FaArrowAltCircleDown } from "react-icons/fa";
import { GiFeather } from "react-icons/gi";

// Importaciones de componentes, funciones y modelos
import { update, getDataBaseJSON } from "../../../../Services/ABMService";
import Category from "../../../../Models/Category";
import NewCategoryButton from "../NewCategoryButton";
import CategoryForm from "./CategoryForm";
import { GenericTable, Column } from "../../../Shared/Generic/Table/Table";

// Importaciones de assets
import image1 from "../../../../assets/images/image1"

// Importacion de estilos
import "./ListCategories.css"
```


**[[⬆ Subir]](#tabla-de-contenidos)**


### <a name='frontend-general-interfaces'>Interfaces y Enums</a>

Debajo de los imports se definiran los interfaces y enums necesarios para el manejo de tipos, estos deberan estar comentados, explicando para que es cada atributo.

Ejemplo:
```
/**
 * Propiedades del componente CategoryList.
 * @prop {Category[]} categories - Un array de objetos Category que representa las categorías.
 * @prop {number | null} selectedCategory - El ID de la categoría seleccionada, o null si no hay ninguna seleccionada.
 * @prop {(categoryId: number) => void} onCategoryClick - Función de devolución de llamada que se ejecuta cuando se hace clic en una categoría. Recibe el ID de la categoría como argumento.
 */
interface CategoryListProps {
  categories: Category[];
  selectedCategory: number | null;
  onCategoryClick: (categoryId: number) => void;
}
```


**[[⬆ Subir]](#tabla-de-contenidos)**


### <a name='frontend-general-componentes'>Componente</a>

Los componentes deberan tener un comentario que explique el funcionamiento general del mismo para que se tenga una idea general antes de ver el codigo.

Ejemplo:

```
/*
 * Componente para mostrar un formulario de categorías.
 * El componente muestra un formulario que permite crear o editar una categoría.
 * Utiliza los estados `category` para almacenar la categoría actual,
 * `errorName` para indicar si hay un error en el campo de nombre,
 * y `select` para almacenar el valor seleccionado en el campo de selección.
 * El formulario se muestra dentro de un modal, y se controla su visibilidad mediante el estado `show`.
 * El ID de la categoría seleccionada se recibe a través de `idCategory`.
 * La función `loadCategories` se utiliza para cargar las categorías actualizadas después de guardar o actualizar una categoría.
 * La función `handleClose` se encarga de cerrar el formulario.
 * La función `saveOrUpdateCategory` se utiliza para guardar o actualizar una categoría llamando a la API correspondiente.
 * La función `getCategoryById` obtiene una categoría por su ID llamando a la API.
 * La función `getCategory` se encarga de obtener la categoría actual (por ID o una nueva).
 * La función `handleIdCategoryChange` maneja el cambio en el campo de selección de tipo de categoría.
 * La función `handleNameChange` maneja el cambio en el campo de nombre de la categoría.
 * La función `handleSubmit` se ejecuta al enviar el formulario y se encarga de guardar o actualizar la categoría.
 */
const CategoryForm = ({ show, setShow, idCategory, loadCategories }: Props) => {
// codigo del componente
}
```


**[[⬆ Subir]](#tabla-de-contenidos)**


#### Nombres de los componentes

Los nombres de los componentes deben seguir el estilo PascalCase. Esto implica que cada palabra comienza con una letra mayúscula, incluida la primera palabra.

#### <a name='frontend-general-componente-nombres'>Nombres y orden dentro del Componente</a>

En el componente existira una regla de ordenamiento que es la siguiente:
```
Estados
Efectos
Funciones (apis y funcionalidades)
Handles (manejadores)
```

##### Estados

Los estados deberan estar identificados en el componente con un comentario que diga "Estados necesarios del componente", los nombres de los estados deberan estar en camelCase, con comentarios explicando su funcion.

Ejemplo:
```
// Estados necesarios del componente
  const [category, setCategory] = useState<Category>(new Category()); // Estado para la categoría actual
  const [errorName, setErrorName] = useState(false); // Estado para indicar si hay un error
  const [select, setSelect] = useState(""); // Estado para el valor seleccionado en el campo de selección
```

##### Efectos

Los efectos deberan estar identificados en el componente con un comentario que explique su funcionamiento general, los nombres de los efectos deberan estar en camelCase, con comentarios explicando su funcion interna si es necesario.

Ejemplo:
```
// Efecto para cargar la categoría al cargar el componente
useEffect(() => {
  getCategory();
}, []);
```

##### Funciones

Las funciones deberan estar identificadas en el componente con un comentario que explique su funcionamiento general, los nombres de las funciones deberan estar en camelCase, con comentarios explicando su funcion interna si es necesario.

Ejemplo:
```
// Función para insertar o actualizar una categoría
const saveOrUpdateCategory = async (categoryToSaveOrUpdate: Category) => {
  try {
    const urlUpdate = "/api/categories/update/"; // URL de la api para actualizar una categoría
    const urlInsert = "/api/categories/save"; // URL de la api para insertar una categoría
    await saveOrUpdate(urlInsert, urlUpdate, categoryToSaveOrUpdate); // Guardar o actualizar una categoría
    await loadCategories(); // Cargar las categorías actualizadas
  } catch (error) {
    // Manejar el error adecuadamente
    console.error("Error al guardar o actualizar la categoria:", error);
  }
};
```

##### Handles

Los handles deberan estar identificados en el componente con un comentario que explique su funcionamiento general, los nombres de los handles deberan estar en camelCase, con comentarios explicando su funcion interna si es necesario.

Ejemplo:
```
// Manejador para manejar el cambio en el campo de selección de tipo de categoría
const handleIdCategoryChange = (
  event: React.ChangeEvent<HTMLSelectElement>
  ) => {
    const selectedValue: string = event.target.value;
    setSelect(selectedValue); // Actualizar el estado del valor seleccionado en el campo de selección
    const isProduct: boolean = selectedValue === "2"; // Verificar si el valor seleccionado es "2"
    setCategory({ ...category, tipo: isProduct }); // Actualizar el estado de la categoría con el tipo correspondiente
    console.log(isProduct ? "Tipo: Producto" : "Tipo: Ingrediente");
};
```


**[[⬆ Subir]](#tabla-de-contenidos)**


#### <a name='frontend-general-componente-renderizado'>Renderizado del Componente</a>

El renderizado del componente debera estar identificado con un comentario como se vera en el ejemplo, no se podran poner comentarios dentro del renderizado, exceptuando el caso de que sean temporales.

```
// Estados, efectos, funciones, handles

// Renderizado del componente
return (
  <ExampleComponent 
  prop1 = {prop1}
  prop2 = {prop2}
  />
)
```


**[[⬆ Subir]](#tabla-de-contenidos)**


#### <a name='frontend-general-componentes-classnames'>Reglas para classNames</a>

Los nombres de los classNames deben seguir el estilo kebab-case para tener mas claridad en los estilos y para facilitar el mantenimiento de los mismos en todo el codigo del proyecto.

```
const Footer = () => {
  // Renderizado del componente
  return (
    <footer>
      <Container fluid className="container-footer">
        <Row>
          <Col className="social-icons-col">
            <div className="circle-icon-container">
              <FaFacebookF className="icon-footer-fb" />
            </div>
            <div className="circle-icon-container">
              <FaTwitter className="icon-footer-tw" />
            </div>
            <div className="circle-icon-container">
              <FaInstagram className="icon-footer-ig" />
            </div>
          </Col>
    </footer>
  );
};
```

Se recomienda evitar el uso de #id en los estilos, ya que React maneja la manipulación del DOM de manera virtual y el uso de identificadores únicos puede generar conflictos y dificultades en el rendimiento de la aplicación.


**[[⬆ Subir]](#tabla-de-contenidos)**


#### <a name='frontend-general-exportaciones'>Exportaciones</a>

Ya sea un archivo TypeScript o un componente se seguiran las siguientes reglas para las exportaciones:

+ Normalmente se utilizara el export al final del archivo, esto incluye funciones, clases, modelos

Ejemplo:
```
// codigo del componente
export default CategoryForm;

// codigo del modelo Category
export default Category

// codigo del ABMSrvice
export { getDataBaseJSON, getJsonById, saveOrUpdate, update, search, deleteById };
```
+ Hay casos excepcionales como interfaces, enums o funciones donde se utilizara el export antes de declararlos

Ejemplo:
```
export interface Column {
  label: string;
  width?: string | number;
}
```


**[[⬆ Subir]](#tabla-de-contenidos)**

Las convenciones no son definitivas, ante cualquier duda o sugerencia de cambio, comunicarse con el equipo de desarrollo para coordinar los cambios o para obtener explicaciones sobre las convenciones.
