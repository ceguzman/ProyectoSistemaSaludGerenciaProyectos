import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicalProcedures } from '../medical-procedures.model';
import { MedicalProceduresService } from '../service/medical-procedures.service';

const medicalProceduresResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicalProcedures> => {
  const id = route.params['id'];
  if (id) {
    return inject(MedicalProceduresService)
      .find(id)
      .pipe(
        mergeMap((medicalProcedures: HttpResponse<IMedicalProcedures>) => {
          if (medicalProcedures.body) {
            return of(medicalProcedures.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default medicalProceduresResolve;
