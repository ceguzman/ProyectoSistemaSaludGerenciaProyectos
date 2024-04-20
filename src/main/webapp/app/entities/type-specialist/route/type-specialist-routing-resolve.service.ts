import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeSpecialist } from '../type-specialist.model';
import { TypeSpecialistService } from '../service/type-specialist.service';

const typeSpecialistResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeSpecialist> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeSpecialistService)
      .find(id)
      .pipe(
        mergeMap((typeSpecialist: HttpResponse<ITypeSpecialist>) => {
          if (typeSpecialist.body) {
            return of(typeSpecialist.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeSpecialistResolve;
