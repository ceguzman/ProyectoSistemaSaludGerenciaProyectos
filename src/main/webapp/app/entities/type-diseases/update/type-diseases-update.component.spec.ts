import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TypeDiseasesService } from '../service/type-diseases.service';
import { ITypeDiseases } from '../type-diseases.model';
import { TypeDiseasesFormService } from './type-diseases-form.service';

import { TypeDiseasesUpdateComponent } from './type-diseases-update.component';

describe('TypeDiseases Management Update Component', () => {
  let comp: TypeDiseasesUpdateComponent;
  let fixture: ComponentFixture<TypeDiseasesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeDiseasesFormService: TypeDiseasesFormService;
  let typeDiseasesService: TypeDiseasesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TypeDiseasesUpdateComponent],
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
      .overrideTemplate(TypeDiseasesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeDiseasesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeDiseasesFormService = TestBed.inject(TypeDiseasesFormService);
    typeDiseasesService = TestBed.inject(TypeDiseasesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeDiseases: ITypeDiseases = { id: 456 };

      activatedRoute.data = of({ typeDiseases });
      comp.ngOnInit();

      expect(comp.typeDiseases).toEqual(typeDiseases);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeDiseases>>();
      const typeDiseases = { id: 123 };
      jest.spyOn(typeDiseasesFormService, 'getTypeDiseases').mockReturnValue(typeDiseases);
      jest.spyOn(typeDiseasesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeDiseases });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeDiseases }));
      saveSubject.complete();

      // THEN
      expect(typeDiseasesFormService.getTypeDiseases).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeDiseasesService.update).toHaveBeenCalledWith(expect.objectContaining(typeDiseases));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeDiseases>>();
      const typeDiseases = { id: 123 };
      jest.spyOn(typeDiseasesFormService, 'getTypeDiseases').mockReturnValue({ id: null });
      jest.spyOn(typeDiseasesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeDiseases: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeDiseases }));
      saveSubject.complete();

      // THEN
      expect(typeDiseasesFormService.getTypeDiseases).toHaveBeenCalled();
      expect(typeDiseasesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeDiseases>>();
      const typeDiseases = { id: 123 };
      jest.spyOn(typeDiseasesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeDiseases });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeDiseasesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
