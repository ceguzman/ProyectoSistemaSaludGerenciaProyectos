import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicationRequest } from '../medication-request.model';
import { MedicationRequestService } from '../service/medication-request.service';

const medicationRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicationRequest> => {
  const id = route.params['id'];
  if (id) {
    return inject(MedicationRequestService)
      .find(id)
      .pipe(
        mergeMap((medicationRequest: HttpResponse<IMedicationRequest>) => {
          if (medicationRequest.body) {
            return of(medicationRequest.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default medicationRequestResolve;
