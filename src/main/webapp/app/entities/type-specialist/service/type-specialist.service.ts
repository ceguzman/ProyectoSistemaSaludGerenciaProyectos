import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeSpecialist, NewTypeSpecialist } from '../type-specialist.model';

export type PartialUpdateTypeSpecialist = Partial<ITypeSpecialist> & Pick<ITypeSpecialist, 'id'>;

export type EntityResponseType = HttpResponse<ITypeSpecialist>;
export type EntityArrayResponseType = HttpResponse<ITypeSpecialist[]>;

@Injectable({ providedIn: 'root' })
export class TypeSpecialistService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-specialists');

  create(typeSpecialist: NewTypeSpecialist): Observable<EntityResponseType> {
    return this.http.post<ITypeSpecialist>(this.resourceUrl, typeSpecialist, { observe: 'response' });
  }

  update(typeSpecialist: ITypeSpecialist): Observable<EntityResponseType> {
    return this.http.put<ITypeSpecialist>(`${this.resourceUrl}/${this.getTypeSpecialistIdentifier(typeSpecialist)}`, typeSpecialist, {
      observe: 'response',
    });
  }

  partialUpdate(typeSpecialist: PartialUpdateTypeSpecialist): Observable<EntityResponseType> {
    return this.http.patch<ITypeSpecialist>(`${this.resourceUrl}/${this.getTypeSpecialistIdentifier(typeSpecialist)}`, typeSpecialist, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeSpecialist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeSpecialist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeSpecialistIdentifier(typeSpecialist: Pick<ITypeSpecialist, 'id'>): number {
    return typeSpecialist.id;
  }

  compareTypeSpecialist(o1: Pick<ITypeSpecialist, 'id'> | null, o2: Pick<ITypeSpecialist, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeSpecialistIdentifier(o1) === this.getTypeSpecialistIdentifier(o2) : o1 === o2;
  }

  addTypeSpecialistToCollectionIfMissing<Type extends Pick<ITypeSpecialist, 'id'>>(
    typeSpecialistCollection: Type[],
    ...typeSpecialistsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeSpecialists: Type[] = typeSpecialistsToCheck.filter(isPresent);
    if (typeSpecialists.length > 0) {
      const typeSpecialistCollectionIdentifiers = typeSpecialistCollection.map(typeSpecialistItem =>
        this.getTypeSpecialistIdentifier(typeSpecialistItem),
      );
      const typeSpecialistsToAdd = typeSpecialists.filter(typeSpecialistItem => {
        const typeSpecialistIdentifier = this.getTypeSpecialistIdentifier(typeSpecialistItem);
        if (typeSpecialistCollectionIdentifiers.includes(typeSpecialistIdentifier)) {
          return false;
        }
        typeSpecialistCollectionIdentifiers.push(typeSpecialistIdentifier);
        return true;
      });
      return [...typeSpecialistsToAdd, ...typeSpecialistCollection];
    }
    return typeSpecialistCollection;
  }
}
