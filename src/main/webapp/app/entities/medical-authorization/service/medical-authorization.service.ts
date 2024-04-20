import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicalAuthorization, NewMedicalAuthorization } from '../medical-authorization.model';

export type PartialUpdateMedicalAuthorization = Partial<IMedicalAuthorization> & Pick<IMedicalAuthorization, 'id'>;

type RestOf<T extends IMedicalAuthorization | NewMedicalAuthorization> = Omit<T, 'dateAuthorization'> & {
  dateAuthorization?: string | null;
};

export type RestMedicalAuthorization = RestOf<IMedicalAuthorization>;

export type NewRestMedicalAuthorization = RestOf<NewMedicalAuthorization>;

export type PartialUpdateRestMedicalAuthorization = RestOf<PartialUpdateMedicalAuthorization>;

export type EntityResponseType = HttpResponse<IMedicalAuthorization>;
export type EntityArrayResponseType = HttpResponse<IMedicalAuthorization[]>;

@Injectable({ providedIn: 'root' })
export class MedicalAuthorizationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medical-authorizations');

  create(medicalAuthorization: NewMedicalAuthorization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalAuthorization);
    return this.http
      .post<RestMedicalAuthorization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(medicalAuthorization: IMedicalAuthorization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalAuthorization);
    return this.http
      .put<RestMedicalAuthorization>(`${this.resourceUrl}/${this.getMedicalAuthorizationIdentifier(medicalAuthorization)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(medicalAuthorization: PartialUpdateMedicalAuthorization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalAuthorization);
    return this.http
      .patch<RestMedicalAuthorization>(`${this.resourceUrl}/${this.getMedicalAuthorizationIdentifier(medicalAuthorization)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMedicalAuthorization>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMedicalAuthorization[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicalAuthorizationIdentifier(medicalAuthorization: Pick<IMedicalAuthorization, 'id'>): number {
    return medicalAuthorization.id;
  }

  compareMedicalAuthorization(o1: Pick<IMedicalAuthorization, 'id'> | null, o2: Pick<IMedicalAuthorization, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicalAuthorizationIdentifier(o1) === this.getMedicalAuthorizationIdentifier(o2) : o1 === o2;
  }

  addMedicalAuthorizationToCollectionIfMissing<Type extends Pick<IMedicalAuthorization, 'id'>>(
    medicalAuthorizationCollection: Type[],
    ...medicalAuthorizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicalAuthorizations: Type[] = medicalAuthorizationsToCheck.filter(isPresent);
    if (medicalAuthorizations.length > 0) {
      const medicalAuthorizationCollectionIdentifiers = medicalAuthorizationCollection.map(medicalAuthorizationItem =>
        this.getMedicalAuthorizationIdentifier(medicalAuthorizationItem),
      );
      const medicalAuthorizationsToAdd = medicalAuthorizations.filter(medicalAuthorizationItem => {
        const medicalAuthorizationIdentifier = this.getMedicalAuthorizationIdentifier(medicalAuthorizationItem);
        if (medicalAuthorizationCollectionIdentifiers.includes(medicalAuthorizationIdentifier)) {
          return false;
        }
        medicalAuthorizationCollectionIdentifiers.push(medicalAuthorizationIdentifier);
        return true;
      });
      return [...medicalAuthorizationsToAdd, ...medicalAuthorizationCollection];
    }
    return medicalAuthorizationCollection;
  }

  protected convertDateFromClient<T extends IMedicalAuthorization | NewMedicalAuthorization | PartialUpdateMedicalAuthorization>(
    medicalAuthorization: T,
  ): RestOf<T> {
    return {
      ...medicalAuthorization,
      dateAuthorization: medicalAuthorization.dateAuthorization?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMedicalAuthorization: RestMedicalAuthorization): IMedicalAuthorization {
    return {
      ...restMedicalAuthorization,
      dateAuthorization: restMedicalAuthorization.dateAuthorization ? dayjs(restMedicalAuthorization.dateAuthorization) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMedicalAuthorization>): HttpResponse<IMedicalAuthorization> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMedicalAuthorization[]>): HttpResponse<IMedicalAuthorization[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
