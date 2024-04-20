import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClinicHistory, NewClinicHistory } from '../clinic-history.model';

export type PartialUpdateClinicHistory = Partial<IClinicHistory> & Pick<IClinicHistory, 'id'>;

type RestOf<T extends IClinicHistory | NewClinicHistory> = Omit<T, 'dateClinic'> & {
  dateClinic?: string | null;
};

export type RestClinicHistory = RestOf<IClinicHistory>;

export type NewRestClinicHistory = RestOf<NewClinicHistory>;

export type PartialUpdateRestClinicHistory = RestOf<PartialUpdateClinicHistory>;

export type EntityResponseType = HttpResponse<IClinicHistory>;
export type EntityArrayResponseType = HttpResponse<IClinicHistory[]>;

@Injectable({ providedIn: 'root' })
export class ClinicHistoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clinic-histories');

  create(clinicHistory: NewClinicHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clinicHistory);
    return this.http
      .post<RestClinicHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(clinicHistory: IClinicHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clinicHistory);
    return this.http
      .put<RestClinicHistory>(`${this.resourceUrl}/${this.getClinicHistoryIdentifier(clinicHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(clinicHistory: PartialUpdateClinicHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clinicHistory);
    return this.http
      .patch<RestClinicHistory>(`${this.resourceUrl}/${this.getClinicHistoryIdentifier(clinicHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestClinicHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestClinicHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClinicHistoryIdentifier(clinicHistory: Pick<IClinicHistory, 'id'>): number {
    return clinicHistory.id;
  }

  compareClinicHistory(o1: Pick<IClinicHistory, 'id'> | null, o2: Pick<IClinicHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getClinicHistoryIdentifier(o1) === this.getClinicHistoryIdentifier(o2) : o1 === o2;
  }

  addClinicHistoryToCollectionIfMissing<Type extends Pick<IClinicHistory, 'id'>>(
    clinicHistoryCollection: Type[],
    ...clinicHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clinicHistories: Type[] = clinicHistoriesToCheck.filter(isPresent);
    if (clinicHistories.length > 0) {
      const clinicHistoryCollectionIdentifiers = clinicHistoryCollection.map(clinicHistoryItem =>
        this.getClinicHistoryIdentifier(clinicHistoryItem),
      );
      const clinicHistoriesToAdd = clinicHistories.filter(clinicHistoryItem => {
        const clinicHistoryIdentifier = this.getClinicHistoryIdentifier(clinicHistoryItem);
        if (clinicHistoryCollectionIdentifiers.includes(clinicHistoryIdentifier)) {
          return false;
        }
        clinicHistoryCollectionIdentifiers.push(clinicHistoryIdentifier);
        return true;
      });
      return [...clinicHistoriesToAdd, ...clinicHistoryCollection];
    }
    return clinicHistoryCollection;
  }

  protected convertDateFromClient<T extends IClinicHistory | NewClinicHistory | PartialUpdateClinicHistory>(clinicHistory: T): RestOf<T> {
    return {
      ...clinicHistory,
      dateClinic: clinicHistory.dateClinic?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restClinicHistory: RestClinicHistory): IClinicHistory {
    return {
      ...restClinicHistory,
      dateClinic: restClinicHistory.dateClinic ? dayjs(restClinicHistory.dateClinic) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestClinicHistory>): HttpResponse<IClinicHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestClinicHistory[]>): HttpResponse<IClinicHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
