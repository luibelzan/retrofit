package com.celnet.retrofit.service;

import com.celnet.retrofit.dto.ETLDatos;
import com.celnet.retrofit.model.TFicheros;
import com.celnet.retrofit.model.TGeneral;
import com.celnet.retrofit.repository.TGeneralRepository;
import com.celnet.retrofit.repository.TFicherosRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ETLService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TGeneralRepository tGeneralRepository;

    @Autowired
    private TFicherosRepository tFicherosRepository;

    private static final int TAMANO_BATCH = 150; // Tamaño del lote

    private void validarFormatoFichero(MultipartFile fichero) {
        String fileName = fichero.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Formato de fichero inválido. Debe ser un archivo Excel (.xlsx)");
        }
    }

    public List<TGeneral> leerExcel(MultipartFile ficheroExcel, String codDistribuidora) throws IOException, ParseException {
        List<TGeneral> registros = new ArrayList<>();

        // Determinar el tipo de formato basado en codDistribuidora
        String tipoFormato;
        if ("777".equals(codDistribuidora)) {
            tipoFormato = "Retrofit";
        } else if ("888".equals(codDistribuidora) || "999".equals(codDistribuidora)) {
            tipoFormato = "Garantia";
        } else {
            throw new IllegalArgumentException("Código de distribuidora no válido: " + codDistribuidora);
        }

        Workbook workbook = WorkbookFactory.create(ficheroExcel.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            // Saltar la primera fila si es el encabezado
            if (row.getRowNum() == 0) continue;

            TGeneral registro = new TGeneral();
            registro.setCodDistribuidora(codDistribuidora); // Asignar el código de distribuidora

            registro.setFecCarga(new Date());

            // Leer y asignar datos según el tipo de formato
            if ("Retrofit".equals(tipoFormato)) {
                // Obtener el número total de celdas en la fila
                int numCeldasTotales = row.getLastCellNum();

                // Validar que haya al menos 8 celdas (una puede estar vacía)
                if (numCeldasTotales < 8) {
                    throw new IllegalArgumentException("El archivo no tiene el formato correcto para Retrofit. "
                            + "Fila: " + row.getRowNum()
                            + ", Celdas totales: " + numCeldasTotales
                            + ". Verifique que tenga todos los campos requeridos.");
                }
                registro.setTipEquipo(row.getCell(1).getStringCellValue());
                registro.setIdContador(row.getCell(4).getStringCellValue());
                registro.setFecEnvio(convertirStringADate(row.getCell(5).getStringCellValue()));
                //registro.setFecRecepcion(convertirStringADate(row.getCell(6).getStringCellValue()));
                registro.setDesAveria(row.getCell(7).getStringCellValue());
            } else if ("Garantia".equals(tipoFormato)) {
                // Obtener el número total de celdas (incluidas vacías)
                int numCeldasTotales = row.getLastCellNum();

                // Validar el número de celdas y lanzar excepción si no coincide
                if (numCeldasTotales < 9) { // Mantener la validación en 9 para Garantía
                    throw new IllegalArgumentException("El archivo no tiene el formato correcto para Garantía. "
                            + "Fila: " + row.getRowNum()
                            + ", Celdas totales: " + numCeldasTotales
                            + ". Verifique que tenga todos los campos requeridos.");
                }
                registro.setTipEquipo(row.getCell(1).getStringCellValue());
                registro.setIdContador(row.getCell(3).getStringCellValue());
                registro.setFecEnvio(convertirStringADate(row.getCell(4).getStringCellValue()));
                //registro.setFecRecepcion(convertirStringADate(row.getCell(5).getStringCellValue()));
                registro.setFecAveria(convertirStringADate(row.getCell(6).getStringCellValue()));
                registro.setDesAveria(row.getCell(7).getStringCellValue());
                registro.setDesObservaciones(row.getCell(8).getStringCellValue());
            }

            registros.add(registro);
        }

        workbook.close();
        return registros;
    }

    public String convertirNumeroAString(Number numero) {
        if (numero == null) {
            return null;
        }
        return String.valueOf(numero);
    }

    public Date convertirStringADate(String fechaStr) throws ParseException {
        if (fechaStr == null || fechaStr.isEmpty()) {
            return null;
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        return formatoFecha.parse(fechaStr);
    }

    private List<TGeneral> limpiarYValidarRegistros(List<TGeneral> registros) {
        List<TGeneral> registrosValidos = new ArrayList<>();
        List<String> errores = new ArrayList<>(); // Lista para almacenar errores

        for (TGeneral registro : registros) {
            // Limpieza de espacios
            if (registro.getIdContador() != null) {
                registro.setIdContador(registro.getIdContador().trim());
            }

            // Validaciones para Id Contador
            if (registro.getIdContador() == null || registro.getIdContador().isEmpty()) {
                errores.add("Fila " + registros.indexOf(registro) + ": El campo Id Contador no puede estar vacío.");
                continue;
            }

            if (registro.getIdContador().length() < 18) {
                errores.add("Fila " + registros.indexOf(registro) + ": El campo Id Contador debe tener al menos 18 caracteres.");
                continue;
            }

            // Truncar a 20 caracteres si es necesario
            if (registro.getIdContador().length() > 18) {
                registro.setIdContador(registro.getIdContador().substring(0, 18));
            }

            // Validar caracteres alfanuméricos
            if (!registro.getIdContador().matches("^[a-zA-Z0-9]*$")) {
                errores.add("Fila " + registros.indexOf(registro) + ": El campo Id Contador debe ser alfanumérico.");
                continue;
            }

            registrosValidos.add(registro);
        }

        // Imprimir errores al final si hay alguno
        if (!errores.isEmpty()) {
            for (String error : errores) {
                System.err.println(error);
            }
        }

        return registrosValidos;
    }

    private int insertarRegistrosPorLote(List<TGeneral> registros) {
        int noDuplicados = 0;
        Set<String> idContadoresExistentes = new HashSet<>(tGeneralRepository.findAllIdContadores()); // Cargar IDs existentes

        List<TGeneral> batchInsertList = new ArrayList<>(TAMANO_BATCH);

        for (TGeneral registro : registros) {
            //System.out.println("Verificando si el registro con IdContador " + registro.getIdContador() + " es duplicado.");
            registro.setFecCarga(new Date());

            // Verificar si ya existe un registro con el mismo IdContador
            if (!idContadoresExistentes.contains(registro.getIdContador())) {
                batchInsertList.add(registro);
                noDuplicados++;

                // Ejecutar el batch cada TAMANO_BATCH registros
                if (batchInsertList.size() == TAMANO_BATCH) {
                    tGeneralRepository.saveAll(batchInsertList);
                    batchInsertList.clear();
                }
            }
        }

        // Guardar cualquier registro restante
        if (!batchInsertList.isEmpty()) {
            tGeneralRepository.saveAll(batchInsertList);
        }

        return noDuplicados;
    }

    public TFicheros guardarDatosFichero(
            MultipartFile ficheroExcel, String codDistribuidora,
            List<TGeneral> registrosLeidos, List<TGeneral> registrosValidos, int noDuplicados) {

        // Crear el objeto RegistrosFicheros y asignar los valores
        TFicheros datos = new TFicheros();
        datos.setCodDistribuidora(codDistribuidora);

        // Usamos LocalDateTime.now() para garantizar que fecCarga sea único en cada inserción
        datos.setFecCarga(LocalDateTime.now());

        datos.setNomFichero(ficheroExcel.getOriginalFilename());
        datos.setTamFichero((float) (ficheroExcel.getSize() / 1024));
        datos.setNumRegistros(registrosLeidos.size());
        datos.setNumCargados(noDuplicados);
        datos.setNumInvalidos(registrosLeidos.size() - registrosValidos.size());
        datos.setCodUsuario(1);

        // Definir el tipo de formato basado en el codDistribuidora
        String tipoFormato = "777".equals(codDistribuidora) ? "Retrofit" : "Garantia";
        datos.setTipFicheros(tipoFormato);

        tFicherosRepository.save(datos);

        return datos;
    }

    @Transactional
    public void actualizarProcesoDesdeGeneral() {
        try {
            String updateQuery = "UPDATE public.t_proceso " +
                    "SET fec_averia = t_general.fec_averia, " +
                    "des_averia = t_general.des_averia, " +
                    "des_observaciones = t_general.des_observaciones " +
                    "FROM t_general " +
                    "WHERE t_general.cod_distribuidora = t_proceso.cod_distribuidora " +
                    "AND t_general.id_contador = t_proceso.id_contador " +
                    "AND t_general.cod_distribuidora IS NOT NULL " +
                    "AND t_general.id_contador IS NOT NULL";

            int rowsUpdated = entityManager.createNativeQuery(updateQuery).executeUpdate();
            System.out.println("Filas actualizadas: " + rowsUpdated);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al actualizar: " + e.getMessage());
        }
    }

    @Transactional
    public ETLDatos cargarDatos(MultipartFile ficheroExcel, String codDistribuidora) throws IOException, ParseException {
        validarFormatoFichero(ficheroExcel); // Validar el formato del fichero

        List<TGeneral> registrosLeidos = leerExcel(ficheroExcel, codDistribuidora);
        List<TGeneral> registrosValidos = limpiarYValidarRegistros(registrosLeidos);

        // Insertar registros
        int noDuplicados = insertarRegistrosPorLote(registrosValidos);

        // Guardar datos en t_ficheros
        actualizarProcesoDesdeGeneral();
        TFicheros registroFichero = guardarDatosFichero(ficheroExcel, codDistribuidora, registrosLeidos, registrosValidos, noDuplicados);

        // Crear el objeto ETLDatos y asignar los valores
        ETLDatos datos = new ETLDatos();
        datos.setRegistrosLeidos(registrosLeidos.size());
        datos.setRegistrosValidos(registrosValidos.size());
        datos.setRegistrosInsertados(noDuplicados);

        System.out.println("Registros leídos: " + registrosLeidos.size());
        System.out.println("Registros validados: " + registrosValidos.size());
        System.out.println("Registros insertados: " + noDuplicados);

        return datos;
    }
}