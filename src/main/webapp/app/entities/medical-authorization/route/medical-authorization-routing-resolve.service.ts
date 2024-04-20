import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicalAuthorization } from '../medical-authorization.model';
import { MedicalAuthorizationService } from '../service/medical-authorization.service';

const medicalAuthorizationResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicalAuthorization> => {
  const id = route.params['id'];
  if (id) {
    return inject(MedicalAuthorizationService)
      .find(id)
      .pipe(
        mergeMap((medicalAuthorization: HttpResponse<IMedicalAuthorization>) => {
          if (medicalAuthorization.body) {
            return of(medicalAuthorization.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default medicalAuthorizationResolve;
