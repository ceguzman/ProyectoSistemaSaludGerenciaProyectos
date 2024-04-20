import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IClinicHistory } from 'app/entities/clinic-history/clinic-history.model';
import { ClinicHistoryService } from 'app/entities/clinic-history/service/clinic-history.service';
import { MedicalAuthorizationService } from '../service/medical-authorization.service';
import { IMedicalAuthorization } from '../medical-authorization.model';
import { MedicalAuthorizationFormService } from './medical-authorization-form.service';

import { MedicalAuthorizationUpdateComponent } from './medical-authorization-update.component';

describe('MedicalAuthorization Management Update Component', () => {
  let comp: MedicalAuthorizationUpdateComponent;
  let fixture: ComponentFixture<MedicalAuthorizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalAuthorizationFormService: MedicalAuthorizationFormService;
  let medicalAuthorizationService: MedicalAuthorizationService;
  let clinicHistoryService: ClinicHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MedicalAuthorizationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MedicalAuthorizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalAuthorizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalAuthorizationFormService = TestBed.inject(MedicalAuthorizationFormService);
    medicalAuthorizationService = TestBed.inject(MedicalAuthorizationService);
    clinicHistoryService = TestBed.inject(ClinicHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ClinicHistory query and add missing value', () => {
      const medicalAuthorization: IMedicalAuthorization = { id: 456 };
      const clinicHistory: IClinicHistory = { id: 22732 };
      medicalAuthorization.clinicHistory = clinicHistory;

      const clinicHistoryCollection: IClinicHistory[] = [{ id: 3488 }];
      jest.spyOn(clinicHistoryService, 'query').mockReturnValue(of(new HttpResponse({ body: clinicHistoryCollection })));
      const additionalClinicHistories = [clinicHistory];
      const expectedCollection: IClinicHistory[] = [...additionalClinicHistories, ...clinicHistoryCollection];
      jest.spyOn(clinicHistoryService, 'addClinicHistoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalAuthorization });
      comp.ngOnInit();

      expect(clinicHistoryService.query).toHaveBeenCalled();
      expect(clinicHistoryService.addClinicHistoryToCollectionIfMissing).toHaveBeenCalledWith(
        clinicHistoryCollection,
        ...additionalClinicHistories.map(expect.objectContaining),
      );
      expect(comp.clinicHistoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicalAuthorization: IMedicalAuthorization = { id: 456 };
      const clinicHistory: IClinicHistory = { id: 5943 };
      medicalAuthorization.clinicHistory = clinicHistory;

      activatedRoute.data = of({ medicalAuthorization });
      comp.ngOnInit();

      expect(comp.clinicHistoriesSharedCollection).toContain(clinicHistory);
      expect(comp.medicalAuthorization).toEqual(medicalAuthorization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalAuthorization>>();
      const medicalAuthorization = { id: 123 };
      jest.spyOn(medicalAuthorizationFormService, 'getMedicalAuthorization').mockReturnValue(medicalAuthorization);
      jest.spyOn(medicalAuthorizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalAuthorization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalAuthorization }));
      saveSubject.complete();

      // THEN
      expect(medicalAuthorizationFormService.getMedicalAuthorization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalAuthorizationService.update).toHaveBeenCalledWith(expect.objectContaining(medicalAuthorization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalAuthorization>>();
      const medicalAuthorization = { id: 123 };
      jest.spyOn(medicalAuthorizationFormService, 'getMedicalAuthorization').mockReturnValue({ id: null });
      jest.spyOn(medicalAuthorizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalAuthorization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalAuthorization }));
      saveSubject.complete();

      // THEN
      expect(medicalAuthorizationFormService.getMedicalAuthorization).toHaveBeenCalled();
      expect(medicalAuthorizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalAuthorization>>();
      const medicalAuthorization = { id: 123 };
      jest.spyOn(medicalAuthorizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalAuthorization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalAuthorizationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClinicHistory', () => {
      it('Should forward to clinicHistoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clinicHistoryService, 'compareClinicHistory');
        comp.compareClinicHistory(entity, entity2);
        expect(clinicHistoryService.compareClinicHistory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
