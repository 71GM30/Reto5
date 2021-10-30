package com.motorbikes.service;

import com.motorbikes.custom.CountClient;
import com.motorbikes.custom.CountReservation;
import com.motorbikes.custom.DescriptionAmount;
import com.motorbikes.custom.StatusReservation;
import com.motorbikes.model.Reservation;
import com.motorbikes.repository.ReservationRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Servicio reserva
 * @author Grupo34Grupo8
 */
@Service
public class ReservationService {
    /**
     * objeto repositorio reserva
     */
    @Autowired
    private ReservationRepository reservationRepository;
 
    /**
     * obtener todos
     * @return 
     */
    public List<Reservation> getAll(){
       return reservationRepository.getAll();
    }
    
    /**
     * reserva id
     * @param reservationId
     * @return 
     */
    public Optional<Reservation> getReservation(int reservationId) {
        return reservationRepository.getReservation(reservationId);
    }
    
    /**
     * saovar reserva
     * @param reservation
     * @return 
     */
    public Reservation save(Reservation reservation) {
        if (reservation.getIdReservation() == null) {
            return reservationRepository.save(reservation);
        } else {
            Optional<Reservation> reservationUno = reservationRepository.getReservation(reservation.getIdReservation());
            if (reservationUno.isEmpty()) {
                return reservationRepository.save(reservation);
            } else {
                return reservation;
            }
        }
    }
    
    /**
     * actualizar reserva
     * @param reservation
     * @return 
     */
    public Reservation update(Reservation reservation){
        if(reservation.getIdReservation()!=null){
            Optional<Reservation> evento= reservationRepository.getReservation(reservation.getIdReservation());
            if(!evento.isEmpty()){

                if(reservation.getStartDate()!=null){
                    evento.get().setStartDate(reservation.getStartDate());
                }
                if(reservation.getDevolutionDate()!=null){
                    evento.get().setDevolutionDate(reservation.getDevolutionDate());
                }
                if(reservation.getStatus()!=null){
                    evento.get().setStatus(reservation.getStatus());
                }
                reservationRepository.save(evento.get());
                return evento.get();
            }else{
                return reservation;
            }
        }else{
            return reservation;
        }
    }
    /**
     * borrar reservacion
     * @param reservationId
     * @return 
     */
    public boolean delete(int reservationId) {
        Boolean aBoolean = getReservation(reservationId).map(reservation -> {
            reservationRepository.delete(reservation);
            return true;
        }).orElse(false);
        return aBoolean;
    }
    
    public StatusReservation reporteStatusServicio (){
        List<Reservation>completed= reservationRepository.ReservacionStatusRepositorio("completed");
        List<Reservation>cancelled= reservationRepository.ReservacionStatusRepositorio("cancelled");
        
        return new StatusReservation(completed.size(), cancelled.size() );
    }

    public List<Reservation> reporteTiempoServicio (String datoA, String datoB){
        SimpleDateFormat parser = new SimpleDateFormat ("yyyy-MM-dd");
        
        Date datoUno = new Date();
        Date datoDos = new Date();
        
        try{
             datoUno = parser.parse(datoA);
             datoDos = parser.parse(datoB);
        }catch(ParseException evt){
            evt.printStackTrace();
        }if(datoUno.before(datoDos)){
            return reservationRepository.ReservacionTiempoRepositorio(datoUno, datoDos);
        }else{
            return new ArrayList<>();
        
        } 
    }
    
    public List<CountClient> reporteClientesServicio(){
            return reservationRepository.getClientesRepositorio();
        }

}
