import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeDocument } from '../type-document.model';
import { TypeDocumentService } from '../service/type-document.service';

const typeDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeDocument> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeDocumentService)
      .find(id)
      .pipe(
        mergeMap((typeDocument: HttpResponse<ITypeDocument>) => {
          if (typeDocument.body) {
            return of(typeDocument.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeDocumentResolve;
