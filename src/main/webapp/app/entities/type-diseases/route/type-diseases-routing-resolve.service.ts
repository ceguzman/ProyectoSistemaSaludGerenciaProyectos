import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeDiseases } from '../type-diseases.model';
import { TypeDiseasesService } from '../service/type-diseases.service';

const typeDiseasesResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeDiseases> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeDiseasesService)
      .find(id)
      .pipe(
        mergeMap((typeDiseases: HttpResponse<ITypeDiseases>) => {
          if (typeDiseases.body) {
            return of(typeDiseases.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeDiseasesResolve;
