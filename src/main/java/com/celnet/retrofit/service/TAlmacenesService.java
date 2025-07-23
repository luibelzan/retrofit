package com.celnet.retrofit.service;

import com.celnet.retrofit.model.TAlmacenes;
import com.celnet.retrofit.model.id.TAlmacenesId;
import com.celnet.retrofit.repository.TAlmacenesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TAlmacenesService {

    @Autowired
    private TAlmacenesRepository tAlmacenesRepository;

    public List<TAlmacenes> findAll() {
        return tAlmacenesRepository.findAll();
    }

    public Optional<TAlmacenes> findById(TAlmacenesId id) {
        return tAlmacenesRepository.findById(id);
    }

    public TAlmacenes save(TAlmacenes almacen) {
        return tAlmacenesRepository.save(almacen);
    }

    public TAlmacenes update(TAlmacenes almacen) {
        if (tAlmacenesRepository.existsById(new TAlmacenesId(almacen.getCodDistribuidora(), almacen.getCodAlmacen()))) {
            return tAlmacenesRepository.save(almacen);
        }
        throw new RuntimeException("El almacén no existe.");
    }

    public void deleteById(TAlmacenesId id) {
        tAlmacenesRepository.deleteById(id);
    }

    // Nuevo método para obtener la descripción del almacén
    public String obtenerDesAlmacen(String codDistribuidora, Integer codAlmacen) {
        Optional<TAlmacenes> almacen = tAlmacenesRepository.findById(new TAlmacenesId(codDistribuidora, codAlmacen));
        return almacen.map(TAlmacenes::getDesAlmacen).orElse("Desconocido");
    }
}
