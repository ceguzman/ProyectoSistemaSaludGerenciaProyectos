import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IClinicHistory } from '../clinic-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../clinic-history.test-samples';

import { ClinicHistoryService, RestClinicHistory } from './clinic-history.service';

const requireRestSample: RestClinicHistory = {
  ...sampleWithRequiredData,
  dateClinic: sampleWithRequiredData.dateClinic?.format(DATE_FORMAT),
};

describe('ClinicHistory Service', () => {
  let service: ClinicHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IClinicHistory | IClinicHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClinicHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ClinicHistory', () => {
      const clinicHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(clinicHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ClinicHistory', () => {
      const clinicHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(clinicHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ClinicHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ClinicHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ClinicHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClinicHistoryToCollectionIfMissing', () => {
      it('should add a ClinicHistory to an empty array', () => {
        const clinicHistory: IClinicHistory = sampleWithRequiredData;
        expectedResult = service.addClinicHistoryToCollectionIfMissing([], clinicHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clinicHistory);
      });

      it('should not add a ClinicHistory to an array that contains it', () => {
        const clinicHistory: IClinicHistory = sampleWithRequiredData;
        const clinicHistoryCollection: IClinicHistory[] = [
          {
            ...clinicHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClinicHistoryToCollectionIfMissing(clinicHistoryCollection, clinicHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ClinicHistory to an array that doesn't contain it", () => {
        const clinicHistory: IClinicHistory = sampleWithRequiredData;
        const clinicHistoryCollection: IClinicHistory[] = [sampleWithPartialData];
        expectedResult = service.addClinicHistoryToCollectionIfMissing(clinicHistoryCollection, clinicHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clinicHistory);
      });

      it('should add only unique ClinicHistory to an array', () => {
        const clinicHistoryArray: IClinicHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const clinicHistoryCollection: IClinicHistory[] = [sampleWithRequiredData];
        expectedResult = service.addClinicHistoryToCollectionIfMissing(clinicHistoryCollection, ...clinicHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const clinicHistory: IClinicHistory = sampleWithRequiredData;
        const clinicHistory2: IClinicHistory = sampleWithPartialData;
        expectedResult = service.addClinicHistoryToCollectionIfMissing([], clinicHistory, clinicHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clinicHistory);
        expect(expectedResult).toContain(clinicHistory2);
      });

      it('should accept null and undefined values', () => {
        const clinicHistory: IClinicHistory = sampleWithRequiredData;
        expectedResult = service.addClinicHistoryToCollectionIfMissing([], null, clinicHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clinicHistory);
      });

      it('should return initial array if no ClinicHistory is added', () => {
        const clinicHistoryCollection: IClinicHistory[] = [sampleWithRequiredData];
        expectedResult = service.addClinicHistoryToCollectionIfMissing(clinicHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(clinicHistoryCollection);
      });
    });

    describe('compareClinicHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClinicHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareClinicHistory(entity1, entity2);
        const compareResult2 = service.compareClinicHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareClinicHistory(entity1, entity2);
        const compareResult2 = service.compareClinicHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareClinicHistory(entity1, entity2);
        const compareResult2 = service.compareClinicHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
