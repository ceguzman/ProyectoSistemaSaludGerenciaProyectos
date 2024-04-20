import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeDiseases, NewTypeDiseases } from '../type-diseases.model';

export type PartialUpdateTypeDiseases = Partial<ITypeDiseases> & Pick<ITypeDiseases, 'id'>;

export type EntityResponseType = HttpResponse<ITypeDiseases>;
export type EntityArrayResponseType = HttpResponse<ITypeDiseases[]>;

@Injectable({ providedIn: 'root' })
export class TypeDiseasesService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-diseases');

  create(typeDiseases: NewTypeDiseases): Observable<EntityResponseType> {
    return this.http.post<ITypeDiseases>(this.resourceUrl, typeDiseases, { observe: 'response' });
  }

  update(typeDiseases: ITypeDiseases): Observable<EntityResponseType> {
    return this.http.put<ITypeDiseases>(`${this.resourceUrl}/${this.getTypeDiseasesIdentifier(typeDiseases)}`, typeDiseases, {
      observe: 'response',
    });
  }

  partialUpdate(typeDiseases: PartialUpdateTypeDiseases): Observable<EntityResponseType> {
    return this.http.patch<ITypeDiseases>(`${this.resourceUrl}/${this.getTypeDiseasesIdentifier(typeDiseases)}`, typeDiseases, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeDiseases>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeDiseases[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeDiseasesIdentifier(typeDiseases: Pick<ITypeDiseases, 'id'>): number {
    return typeDiseases.id;
  }

  compareTypeDiseases(o1: Pick<ITypeDiseases, 'id'> | null, o2: Pick<ITypeDiseases, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeDiseasesIdentifier(o1) === this.getTypeDiseasesIdentifier(o2) : o1 === o2;
  }

  addTypeDiseasesToCollectionIfMissing<Type extends Pick<ITypeDiseases, 'id'>>(
    typeDiseasesCollection: Type[],
    ...typeDiseasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeDiseases: Type[] = typeDiseasesToCheck.filter(isPresent);
    if (typeDiseases.length > 0) {
      const typeDiseasesCollectionIdentifiers = typeDiseasesCollection.map(typeDiseasesItem =>
        this.getTypeDiseasesIdentifier(typeDiseasesItem),
      );
      const typeDiseasesToAdd = typeDiseases.filter(typeDiseasesItem => {
        const typeDiseasesIdentifier = this.getTypeDiseasesIdentifier(typeDiseasesItem);
        if (typeDiseasesCollectionIdentifiers.includes(typeDiseasesIdentifier)) {
          return false;
        }
        typeDiseasesCollectionIdentifiers.push(typeDiseasesIdentifier);
        return true;
      });
      return [...typeDiseasesToAdd, ...typeDiseasesCollection];
    }
    return typeDiseasesCollection;
  }
}
