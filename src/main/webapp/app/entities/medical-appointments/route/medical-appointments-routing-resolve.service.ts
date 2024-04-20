import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicalAppointments } from '../medical-appointments.model';
import { MedicalAppointmentsService } from '../service/medical-appointments.service';

const medicalAppointmentsResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicalAppointments> => {
  const id = route.params['id'];
  if (id) {
    return inject(MedicalAppointmentsService)
      .find(id)
      .pipe(
        mergeMap((medicalAppointments: HttpResponse<IMedicalAppointments>) => {
          if (medicalAppointments.body) {
            return of(medicalAppointments.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default medicalAppointmentsResolve;
