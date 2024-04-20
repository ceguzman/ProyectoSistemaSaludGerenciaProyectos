import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClinicHistory } from '../clinic-history.model';
import { ClinicHistoryService } from '../service/clinic-history.service';

const clinicHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IClinicHistory> => {
  const id = route.params['id'];
  if (id) {
    return inject(ClinicHistoryService)
      .find(id)
      .pipe(
        mergeMap((clinicHistory: HttpResponse<IClinicHistory>) => {
          if (clinicHistory.body) {
            return of(clinicHistory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default clinicHistoryResolve;
